package wrm.exmo.transform.selector;

import javax.script.ScriptException;

import wrm.exmo.transform.script.IScriptSupportAware;
import wrm.exmo.transform.script.ScriptSupport;

/**
 * a selector that executes a script.
 * The script context knows "source" and "target" object
 * 
 * @author pemucha
 */
public class ScriptSelector implements Selector, IScriptSupportAware {

	private String script;
	private ScriptSupport support;

	public ScriptSelector(String script) {
		this.script = script;

	} 

	
	@Override
	public Object getValue(Object source, Object target) throws Exception {
		return execute(source, target);
	}

	@Override
	public Class<?> getType(Object source, Object target) {
		return null; //type of value will be used
	}

	
	
	
	
	
	public Object execute(Object source, Object target) throws Exception {
		try{
			Object result = support.evaluate(script, false);
			return result;
		} catch (ScriptException se){
			throw new Exception("Evalutation for script failed: " + script, se);
		}
	}


	@Override
	public void setScriptSupport(ScriptSupport support) {
		this.support = support;
		
	}
}
