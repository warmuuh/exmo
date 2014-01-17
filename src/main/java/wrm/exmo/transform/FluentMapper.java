package wrm.exmo.transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.extern.apachecommons.CommonsLog;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.expression.DefaultResolver;

import wrm.exmo.Pair;
import wrm.exmo.transform.converter.ConversionException;
import wrm.exmo.transform.converter.TypeConverter;
import wrm.exmo.transform.script.IScriptSupportAware;
import wrm.exmo.transform.script.ScriptEnvironmentResolver;
import wrm.exmo.transform.script.ScriptSupport;
import wrm.exmo.transform.targetor.PropertyTargetor;
import wrm.exmo.transform.targetor.Targetor;

/**
 * This is the baseclass of a declarative mapping framework with a fluent mapping API.
 * Extending this class allows the definition of Bean-to-Bean mappers.
 * 
 * The framework is rule-based, rules are defined using the fluent DSL. Schematic syntax:
 * 
 * map(Selector).to("targetProperty")[.with(Converter)*][.andDo(Callback)][.onNull(Selector)][.elementsOfType(Class<?>)]
 *	
 * A selector returns a value, if  a object is fed in. (aka selects a value)
 * A converter converts this value, that is, from type a to type b or just value transformation
 * 		Every FluentMapper is a Converter, so you can define a new fluent mapper here to have "sub"-mappers
 * 		There can be multiple converters.
 * A Callback is called after (successful) mapping
 * onNull states, that the value of this selector will be used if the original one returns null.
 * elementsOfType(Class<?>) will map list onto lists and calls the supplied converters on each element instead of on the list											 				 
 * 
 * Several utility methods provide easy creation of Selectors, Converters and Callbacks. See RuleBuilderBase for more infos.
 * 
 * @author pemucha
 *
 * @param <S> source type
 * @param <T> target type
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@CommonsLog
public abstract class FluentMapper<S, T> extends RuleBuilderBase implements TypeConverter<S, T>, IScriptSupportAware  {


	
	Map<Pair<Class, Class>, TypeConverter> globalConverters = new HashMap<Pair<Class, Class>, TypeConverter>();
	private boolean isInitialized;
	Targetor defaultTargetor = new PropertyTargetor();

	protected ScriptSupport scriptSupport;
	


	public FluentMapper() {
		this(null);
	}
	
	public FluentMapper(Environment env) {
		super(env);
		isInitialized = false;
		setScriptSupport( new ScriptSupport() );
		if (env != null)
			scriptSupport.bindValue("$", new ScriptEnvironmentResolver(env), true);
	}
	
	
	/**
	 * rules can be defined here or in the constructor
	 * this will be called only once at the first triggering of mapObject!
	 */
	protected void createRules() throws Exception {};
	
	/**
	 * will be called after successful mapping
	 * @param source object to map
	 * @param target mapped object
	 */
	protected void postMappingCallback(S source, T target) throws Exception{};

	/**
	 * will be called to create a new instance of a target object.
	 * Default constructor will be used. Overwrite to implement custom behavior 
	 */
	protected T createTargetObject(S sourceObject, Class<? extends T> targetClass) throws Exception{
		try {
			return targetClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	public T mapObject(S source, Class<? extends T> targetClass) throws ConversionException {
		if(log.isDebugEnabled())
		{
			String simpleName = source.getClass().getSimpleName();
			if (simpleName.isEmpty()) //could happen for nested classes
				simpleName = source.getClass().getName();
			log.debug("Mapping: " + simpleName + " <-> " + targetClass.getSimpleName());
		}

		if (!isInitialized)
		{
			try {
				createRules();
			} catch (Exception e) {
				throw new ConversionException(e);
			}
			isInitialized = true;
		}
		
		
		scriptSupport.bindValue("source", source, false);
		T target = null;
		try {
			target = createTargetObject(source, targetClass);
		} catch (Exception e1) {
			throw new ConversionException(e1);
		}
		
		if (target == null)
			throw new ConversionException("Target with type " + targetClass.getName() + " could not be created.");
		
		scriptSupport.bindValue("target", target, false);
		
		
		for (TransformationRule rule : rules) {
			try {
				//init scriptings
				initScriptSupport(rule);
				
				
				//select
				Object sourceVal = rule.getSource().getValue(source, target);
				if (sourceVal == null)
					continue;
				Class sourceValClass = rule.getSource().getType(source, target);
				if (sourceValClass == null)
					sourceValClass = sourceVal.getClass();
				
				defaultTargetor.createPathToProperty(target, rule.getDestination());

				Class targetValClass = defaultTargetor.getPropertyType(target, rule.getDestination());
				
				if (targetValClass == null)
					log.warn("Cannot find propertytype for " + rule.getDestination() + " at type " + target.getClass().getName());
				
				Object destinationVal = null;
				
				//map
				if (rule.isDeepMapping() 
						&& Collection.class.isAssignableFrom(sourceValClass)
						&& targetValClass.isAssignableFrom(LinkedList.class)) //for now: just linkedLists are supported
					destinationVal = deepMapping(rule, sourceVal, sourceValClass, target);
				else				
					destinationVal = flatMapping(rule, sourceVal, sourceValClass, targetValClass);

				//set
				defaultTargetor.setValue(target, rule.getDestination(), destinationVal);
				
				//callback
				if (rule.getCallback() != null)
					rule.getCallback().execute(source, target);

			} catch (Exception e) {
				throw new ConversionException("Mapping failed for " + source.getClass()  + " --> " + target.getClass(), e);
			}

		}
		try {
			postMappingCallback(source, target);
		} catch (Exception e) {
			throw new ConversionException(e);
		}
		
		return target;
	}
	
	private void initScriptSupport(TransformationRule rule) {
		scriptSupport.injectScriptSupport(rule.getSource(), false);
		
		for(TypeConverter c : rule.getConverters()){
//			boolean newScope = c instanceof FluentMapper;
			scriptSupport.injectScriptSupport(c, true);
		}
		

		scriptSupport.injectScriptSupport(rule.getCollectionFactory(), false);
		scriptSupport.injectScriptSupport(rule.getCallback(), false);
	}


	private Collection deepMapping(TransformationRule rule, Object sourceVal,
			Class sourceValClass, Object targetVal) throws Exception {
		Collection sourceCol = (Collection)sourceVal;
		
		List destValTemp = null;
		if (rule.getCollectionFactory() != null){
			destValTemp = (List) rule.getCollectionFactory().getValue(sourceVal, targetVal);
			if (destValTemp == null)
				throw new ConversionException("CollectionFactory returned null");
		}
		else
			destValTemp  = new LinkedList();
		
		
		
		for(Object o : sourceCol)
		{
			Object mapping = flatMapping(rule, o, sourceValClass, rule.getDeepMappingType()); 
			destValTemp.add(mapping);
		}
		return destValTemp;
	}
	
	private Object flatMapping(TransformationRule rule, Object sourceVal,
			Class sourceValClass, Class targetValClass) throws ConversionException {
		Object destinationVal = sourceVal;
		for(TypeConverter converter : rule.getConverters()){
			destinationVal = converter.convert(destinationVal, destinationVal.getClass(), targetValClass);
			if (destinationVal == null)
				throw new ConversionException("Converter " + converter + " returned null for sourceValue: " + sourceVal );
		}
		
		//not enough converted now? continue with default conversion
		if (!destinationVal.getClass().equals(targetValClass)) {
			destinationVal = convertTo(destinationVal, destinationVal.getClass(), targetValClass);
		}
		return destinationVal;
	}



	private Object convertTo(Object sourceVal, Class sourceValClass,
			Class targetValClass) throws ConversionException {

		
		TypeConverter converter = findGlobalConverterFor(sourceValClass, targetValClass);
		
		if (converter != null)
			return converter.convert(sourceVal, sourceValClass, targetValClass);

		return ConvertUtils.convert(sourceVal, targetValClass);
	}

	private TypeConverter findGlobalConverterFor(Class sourceClass, Class targetClass) {
		
		for(Entry<Pair<Class, Class>, TypeConverter> e : globalConverters.entrySet()){
			if (e.getKey().getFirst().isAssignableFrom(sourceClass)
					&& targetClass.isAssignableFrom(e.getKey().getSecond()))
				return e.getValue();
		}
		
		return null;
	}
	
	
	
	protected void registerGlobalConverter(TypeConverter converter, Class sourceClass, Class targetClass){
		globalConverters.put(Pair.of(sourceClass, targetClass), converter);
	}
	
	
	@Override
	public T convert(S source, Class<S> sourceValClass, Class<T> targetValClass) throws ConversionException {
			return mapObject(source, targetValClass);
	}

	@Override
	public void setScriptSupport(ScriptSupport support) {
		this.scriptSupport = support;
	}
	
	public void registerGlobalVariable(String name, Object value){
		this.scriptSupport.bindValue(name, value, true);
	}
	
}
