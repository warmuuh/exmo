package wrm.exmo.transform.selector;

import wrm.exmo.transform.script.IScriptSupportAware;
import wrm.exmo.transform.script.ScriptSupport;


public class DefaultSelector implements Selector, IScriptSupportAware {

	private Selector defaultValue;
	private Selector sourceSel;
 
	public DefaultSelector(Selector sourceSel, Selector defaultValue) {
		
		this.sourceSel = sourceSel;
		this.defaultValue = defaultValue;
	}

	@Override
	public Object getValue(Object source, Object target) throws Exception {
		Object r = sourceSel.getValue(source, target);
		
		if (r == null) return defaultValue.getValue(source, target);
		if ((r instanceof String) && ((String)r).isEmpty()) return defaultValue.getValue(source, target);
		
		return r;
	}

	@Override
	public Class<?> getType(Object source, Object target) throws Exception {
		Object val = sourceSel.getValue(source, target);
		if (val == null)
			return defaultValue.getType(source, target);
		
		return sourceSel.getType(source, target);
	}

	@Override
	public void setScriptSupport(ScriptSupport support) {
		support.injectScriptSupport(sourceSel, false);
		support.injectScriptSupport(defaultValue, false);
	}

}
