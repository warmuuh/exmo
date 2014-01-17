package wrm.exmo.transform.converter;

import javax.script.ScriptException;

import wrm.exmo.transform.script.IScriptSupportAware;
import wrm.exmo.transform.script.ScriptSupport;

/**
 * A converter that executes a script.
 * The script context knows "source" object
 * @author pemucha
 *
 */
public class ScriptConverter implements TypeConverter<Object, Object>, IScriptSupportAware {

	
	private String script;
	private ScriptSupport support;
	
	
	public ScriptConverter(String script){
		this.script = script;
	}
	
	

	@Override
	public Object convert(Object source, Class<Object> sourceValClass,
			Class<Object> targetValClass) throws ConversionException {
		
		support.bindValue("value", source, false);

		try {
			Object result = support.evaluate(script, false);
			return result;
		} catch (ScriptException e) {
			throw new ConversionException("Script Evaluation failed for: " + script, e);
		}
		
	}
	
	
	@Override
	public String toString() {
		return "ScriptConverter["+script+"]";
	}



	@Override
	public void setScriptSupport(ScriptSupport support) {
		this.support = support;
		
	}

}
