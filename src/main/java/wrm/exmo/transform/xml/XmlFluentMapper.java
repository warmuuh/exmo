package wrm.exmo.transform.xml;

import java.net.URL;
import java.security.InvalidParameterException;
import java.util.logging.Logger;

import javax.script.ScriptException;

import org.apache.commons.logging.LogFactory;
import org.hamcrest.core.IsEqual;

import lombok.extern.apachecommons.CommonsLog;
import wrm.exmo.transform.Environment;
import wrm.exmo.transform.FluentMapper;
import wrm.exmo.transform.RuleBuilder;
import wrm.exmo.transform.converter.ConversionException;
import wrm.exmo.transform.converter.ScriptConverter;
import wrm.exmo.transform.converter.TypeConverter;
import wrm.exmo.transform.xml.model.TConverter;
import wrm.exmo.transform.xml.model.TMapper;
import wrm.exmo.transform.xml.model.TMapping;
import wrm.exmo.transform.xml.model.TScript;


@CommonsLog
public class XmlFluentMapper<S,T> extends FluentMapper<S, T> {


	private TMapper mapperDefinition = null;
	private String description;
	
	
	public XmlFluentMapper(URL url) {
		this(url, null);
	}

	public XmlFluentMapper(URL url, Environment env) {
		super(env);
		if (url == null)
			throw new InvalidParameterException("url must not be null");
		log.debug("Loading xml mapping definition: " + url);
		this.description = getDescription(url);
		
		mapperDefinition = XmlUtil.parseMapperXml(url);
	}
	
	private String getDescription(URL url) {
		String str = url.toString();
		int idx = str.lastIndexOf('/');
		if (idx >= 0)
			str = str.substring(idx+1);
		idx = str.lastIndexOf('.');
		if (idx >= 0)
			str = str.substring(0, idx);
		
		return str;
	}

	private XmlFluentMapper(TMapper mapper, String description){
		this.description = description;
		mapperDefinition = mapper;
	}
	
	
	@Override
	protected void createRules() throws ScriptException 
	{
		if (mapperDefinition.getMappings().size()  == 0)
			log.warn("Xml mapper definition ["+description+"]does not contain any rules.");
		
		//evaluate all scripts => all function definition etc are registered
		for(TScript script : mapperDefinition.getScripts())
			scriptSupport.evaluate(script.getValue(), script.getName(), true); 
		
		//create mappings
		for(TMapping mapping : mapperDefinition.getMappings())
			createMapping(mapping);
	}


	private void createMapping(TMapping mappingDef) throws ScriptException {
		
		RuleBuilder builder = null;
		
		if (mappingDef.getFrom() != null && !mappingDef.getFrom().isEmpty() )
			builder = map(mappingDef.getFrom());
				 
		if (mappingDef.getExpr() != null && !mappingDef.getExpr().isEmpty() )
			builder = map(expr(mappingDef.getExpr()));
		
		
			
			
		builder.to(mappingDef.getTo());
		
		
		if (mappingDef.getCallback() != null)
			builder.andDo(mappingDef.getCallback());

		if (mappingDef.getOnNull() != null && !mappingDef.getOnNull().isEmpty() )
			builder.onNull(expr(mappingDef.getOnNull()));
		
		if (mappingDef.getElements() != null)
		{	builder.asElementsOfType(mappingDef.getElements());
			if (mappingDef.getCollectionFactoryExpr() != null && !mappingDef.getCollectionFactoryExpr().isEmpty())
				builder.withCollectionFactory(expr(mappingDef.getCollectionFactoryExpr()));
		}
		
		
		for(TConverter converter : mappingDef.getWiths()){
			TypeConverter<?,?> tc = createConverter(converter);
			if (tc != null)
				builder.with(tc);
			
		}
		
	}

	
	@Override
	public T mapObject(S source, Class<? extends T> targetClass)
			throws ConversionException {
		log.debug("Xml-based mapping started: " + description);
		
		scriptSupport.bindValue("log", LogFactory.getLog(getClass().getPackage().getName() + "." + description), true);
		T result = super.mapObject(source, targetClass);
		scriptSupport.unbindValue("log", true);
		
		return result;
	}
	
	private TypeConverter<?,?> createConverter(TConverter converterDef) throws ScriptException {
		
		if ( converterDef.getExpr() != null){
			return new ScriptConverter(converterDef.getExpr());
		} else if (converterDef.getConverter() != null) {
			
			Object converterObj = scriptSupport.evaluate(converterDef.getConverter(), false);
			if (converterObj == null || !(converterObj instanceof TypeConverter))
			{
				log.error("Converter-Expression \"" + converterDef.getConverter() + "\" did not evaluate to converter type. Result: " + converterObj);
				return null;
			}
			return (TypeConverter<?, ?>) converterObj;
		} else if (converterDef.getMappings().size() > 0 ){
			return new XmlFluentMapper<>(converterDef, "submapper of " + description);
		} else if (converterDef.getRef() != null) {
			return new XmlFluentMapper<>(converterDef.getRef(),  "submapper of " + description);
		}
		
		
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected T createTargetObject(S sourceObject,
			Class<? extends T> targetClass) throws Exception {
		
		if (mapperDefinition.getFactoryExpr() != null && !mapperDefinition.getFactoryExpr().isEmpty())
			return (T) scriptSupport.evaluate(mapperDefinition.getFactoryExpr(), false);
		
		
		return super.createTargetObject(sourceObject, targetClass);
	}

	
	@Override
	protected void postMappingCallback(S source, T target) throws Exception {
		if (mapperDefinition.getAndDo() != null && !mapperDefinition.getAndDo().isEmpty())
			scriptSupport.evaluate(mapperDefinition.getAndDo(), false);
		else
			super.postMappingCallback(source, target);
	}
	
	
}
