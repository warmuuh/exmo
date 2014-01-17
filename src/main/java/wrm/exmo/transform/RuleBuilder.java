package wrm.exmo.transform;

import wrm.exmo.transform.callback.RuleCallback;
import wrm.exmo.transform.callback.ScriptCallback;
import wrm.exmo.transform.converter.ScriptConverter;
import wrm.exmo.transform.converter.TypeConverter;
import wrm.exmo.transform.selector.DefaultSelector;
import wrm.exmo.transform.selector.Selector;

/**
 * DSL Object that enables fluent mapping API
 * 
 * @author pemucha
 */
public class RuleBuilder {
	TransformationRule step;
	
	/*package*/ RuleBuilder(Selector from, TransformationRule step) {
		this.step = step;
		step.setSource(from);
	}
	
	/**
	 * The target 
	 */
	public RuleBuilder to(String to){
		step.setDestination(to);
		return this;
	}

	/**
	 * adds a converter to the rule
	 */
	public RuleBuilder with(TypeConverter<?,?> typeConverter) {
		step.getConverters().add(typeConverter);
		return this;
	}
	
	/**
	 * adds a Script Converter to the rule
	 */
	public RuleBuilder with(String script) {
		step.getConverters().add(new ScriptConverter(script));
		return this;
	}
	
	/**
	 * adds a script callback to the rule
	 */
	public RuleBuilder andDo(String script){
		ScriptCallback cb = new ScriptCallback(script);
		step.setCallback(cb);
		return this;
	}
	
	/**
	 * adds a callback to the rule
	 */
	public RuleBuilder andDo(RuleCallback callback){
		step.setCallback(callback);
		return this;
	}

	/**
	 * adds a default selector to the rule
	 */
	public RuleBuilder onNull(wrm.exmo.transform.selector.Selector defSelector) {
		step.setSource(new DefaultSelector(step.getSource(), defSelector));
		return this;
	}
	
	/**
	 * enables deep mapping of collections
	 */
	public RuleBuilder asElementsOfType(Class<?> listElementType){
		step.setDeepMapping(true);
		step.setDeepMappingType(listElementType);
		return this;
	}
	

	/**
	 * enables deep mapping of collections
	 */
	public RuleBuilder withCollectionFactory(Selector selector){
		step.setDeepMapping(true);
		step.setCollectionFactory(selector);
		return this;
	}
	
	
	
}