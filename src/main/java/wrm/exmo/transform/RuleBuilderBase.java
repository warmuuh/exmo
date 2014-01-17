package wrm.exmo.transform;

import java.util.LinkedList;
import java.util.List;

import wrm.exmo.transform.converter.AsListConverter;
import wrm.exmo.transform.converter.TypeConverter;
import wrm.exmo.transform.selector.ContextSelector;
import wrm.exmo.transform.selector.PropertySelector;
import wrm.exmo.transform.selector.ScriptSelector;
import wrm.exmo.transform.selector.Selector;
import wrm.exmo.transform.selector.ValueSelector;

/**
 * Provides utiliy dsl methods to easily create selectors, callbacks etc for mapping
 * 
 * 
 * @author pemucha
 *
 */
public class RuleBuilderBase {

	
	List<TransformationRule> rules = new LinkedList<TransformationRule>();
	private Environment env;
	
	
	
	public RuleBuilderBase(Environment env) {
		this.env = env;
	}
	
	
	
	
	
	/**
	 * DSL Methods
	 * 
	 */

	/**
	 * The entry point to the fluent dsl. 
	 * This method accepts a property name. 
	 * Syntax according to http://commons.apache.org/beanutils/apidocs/org/apache/commons/beanutils/package-summary.html#standard.nested
	 * 
	 * @param from a property description. 
	 * @return a rule builder dsl object
	 */
	protected RuleBuilder map(String from) {
		TransformationRule rule = new TransformationRule();
		rules.add(rule);
		return new RuleBuilder(new PropertySelector(from), rule);
	}

	/**
	 * The entry point to the fluent dsl. 
	 * @param from a generic value selector
	 * @return a rule builder dsl object
	 */
	protected RuleBuilder map(Selector from) {
		TransformationRule rule = new TransformationRule();
		rules.add(rule);
		return new RuleBuilder(from, rule);
	}
	
	
	
	/**
	 * returns a constant selector that returns the given value or the value of the environment variable, 
	 * if the string has following format "${name}"
	 * 
	 * @param defaultValue the value to be used for mapping
	 * @return the constant selector
	 */
	protected Selector value(String defaultOrEnv) {
		Object value = resolveProperty(defaultOrEnv);
		
		return new ValueSelector(value);
	}





	private Object resolveProperty(String defaultOrEnv) {
		Object value= defaultOrEnv;
		if (env != null)
			if (defaultOrEnv.startsWith("${") && defaultOrEnv.endsWith("}")){
				String varName = defaultOrEnv.substring(2, defaultOrEnv.length()-3);
				value = env.resolveVariable(varName);
			}
		return value;
	}
	
	
	/**
	 * returns a constant selector that returns the given value and nothing else
	 * @param defaultValue the value to be used for mapping
	 * @return the constant selector
	 */
	protected Selector value(Object defaultValue) {
		return new ValueSelector(defaultValue);
	}
	
	
	protected Selector value(Object context, String property) {
		return new ContextSelector(context, new PropertySelector(property));
	}
	/**
	 * returns a script-selector. 
	 * Instead of a value, the return value of the script will be used as selected value.
	 * @see ScriptSelector
	 * 
	 * @param script script expression
	 * @return the expression selector
	 */
	protected Selector expr(String script) {
		return new ScriptSelector(script);
	}
	
	
	/**
	 * returns a x-to-list converter that wraps a given object into a new list.
	 * @return the converter
	 */
	protected AsListConverter<Object> asList(){
		return new AsListConverter<Object>();
	}
	
	/**
	 * returns a converter that formats an object (using toString) with a given format
	 * @param format
	 * @return
	 */
	protected TypeConverter<Object, String> format(final String format){
		return new TypeConverter<Object, String>() {
			
			@Override
			public String convert(Object source, Class<Object> sourceValClass,
					Class<String> targetValClass) {
				return String.format(format, source);
			}
		};
	}
	
}
