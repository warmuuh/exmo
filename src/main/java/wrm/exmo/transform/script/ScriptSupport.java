package wrm.exmo.transform.script;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import sun.org.mozilla.javascript.internal.FunctionObject;

import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
public class ScriptSupport {
	ScriptEngineManager mgr;
	ScriptEngine engine;
	
	
	
	public ScriptSupport() {
		mgr = new ScriptEngineManager();
		engine = mgr.getEngineByName("js");
	}
	
	
	private ScriptSupport(ScriptEngineManager mgr, Bindings context) {
		this.mgr = mgr;
		engine = mgr.getEngineByName("js");
		getContext().put("parent", new MapScriptable(context));
	}

	
	public void bindValue(String name, Object value, boolean globalScope){
		int scope = globalScope ? ScriptContext.GLOBAL_SCOPE 
								: ScriptContext.ENGINE_SCOPE;
		
		Bindings bindings = engine.getBindings(scope);
		bindings.put(name, value);
	}
	

	public void unbindValue(String name, boolean globalScope){
		int scope = globalScope ? ScriptContext.GLOBAL_SCOPE 
								: ScriptContext.ENGINE_SCOPE;
		
		Bindings bindings = engine.getBindings(scope);
		bindings.remove(name);
	}

	public Object evaluate(String script, boolean globalScope) throws ScriptException{
		return evaluate(script, null, globalScope);
	}
	
	
	public Object evaluate(String script, String name, boolean globalScope) throws ScriptException{
		
		if (name != null)
			engine.put(ScriptEngine.FILENAME, name);
		else
			engine.getBindings(ScriptContext.ENGINE_SCOPE).remove(ScriptEngine.FILENAME);
		
		Object object = null;
		if (globalScope)
			object =  engine.eval(script, mgr.getBindings());
		else
			object = engine.eval(script);

		return object;
	}
	

	
	private Bindings getContext(){
		return engine.getBindings(ScriptContext.ENGINE_SCOPE);
	}
	
	
	
	public void injectScriptSupport(Object o, boolean newScope){
		if (o == null  || !IScriptSupportAware.class.isAssignableFrom(o.getClass()))
			return;
			
		IScriptSupportAware aware = (IScriptSupportAware) o;
		if (newScope)
			aware.setScriptSupport(new ScriptSupport(this.mgr, this.getContext()));
		else
			aware.setScriptSupport(this);
		
		
	}
	
	
	
}
