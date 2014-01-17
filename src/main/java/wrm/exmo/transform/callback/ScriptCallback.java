package wrm.exmo.transform.callback;

import javax.script.ScriptException;

import wrm.exmo.transform.script.IScriptSupportAware;
import wrm.exmo.transform.script.ScriptSupport;

/**
 * A callback that executes a script.
 * The scriptcontext knows "source" and "target" object
 * 
 * 
 * @author pemucha
 *
 */
public class ScriptCallback implements RuleCallback, IScriptSupportAware {

	
	private String script;
	private ScriptSupport support;
	
	
	public ScriptCallback(String script){
		this.script = script;
	}
	
	@Override
	public void execute(Object source, Object target) throws ScriptException {
		support.evaluate(script, false);
	}

	@Override
	public void setScriptSupport(ScriptSupport support) {
		this.support = support;
	}

}
