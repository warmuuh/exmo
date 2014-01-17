package wrm.exmo.transform.script;
import java.util.HashMap;

import sun.org.mozilla.javascript.internal.ScriptableObject;
import wrm.exmo.transform.Environment;
import sun.org.mozilla.javascript.internal.Scriptable;

public class ScriptEnvironmentResolver implements Scriptable {
	
	private Environment env;

	public ScriptEnvironmentResolver(Environment env) {
		this.env = env;
	}

	@Override
	public String getClassName() {
		return env.getClass().getName();
	}

	@Override
	public Object get(String paramString, Scriptable paramScriptable) {
		return env.resolveVariable(paramString);
	}

	@Override
	public Object get(int paramInt, Scriptable paramScriptable) {
		return null;
	}

	@Override
	public boolean has(String paramString, Scriptable paramScriptable) {
		return env.getRegisteredVariableNames().contains(paramString);
	}

	@Override
	public boolean has(int paramInt, Scriptable paramScriptable) {
		return false;
	}

	@Override
	public void put(String paramString, Scriptable paramScriptable,
			Object paramObject) {
		/*not supported!*/
	}

	@Override
	public void put(int paramInt, Scriptable paramScriptable, Object paramObject) {
		/*not supported!*/
		
	}

	@Override
	public void delete(String paramString) {
		/*not supported!*/
		
	}

	@Override
	public void delete(int paramInt) {
		/*not supported!*/
		
	}

	@Override
	public Scriptable getPrototype() {
		return null;
	}

	@Override
	public void setPrototype(Scriptable paramScriptable) {
	}

	@Override
	public Scriptable getParentScope() {
		return null;
	}

	@Override
	public void setParentScope(Scriptable paramScriptable) {
	}

	@Override
	public Object[] getIds() {
		return env.getRegisteredVariableNames().toArray();
	}

	@Override
	public Object getDefaultValue(Class<?> paramClass) {
		return env.toString();
	}

	@Override
	public boolean hasInstance(Scriptable paramScriptable) {
		return false;
	}


}
