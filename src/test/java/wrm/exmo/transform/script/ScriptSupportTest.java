package wrm.exmo.transform.script;

import static org.junit.Assert.assertEquals;

import javax.script.ScriptException;

import org.junit.Test;

import wrm.exmo.transform.script.IScriptSupportAware;
import wrm.exmo.transform.script.ScriptSupport;
import lombok.Data;





public class ScriptSupportTest {
	
	@Data
	static class Scope implements IScriptSupportAware{
		ScriptSupport support = new ScriptSupport();

		@Override
		public void setScriptSupport(ScriptSupport support) {
			this.support = support;
		}
	}
	
	
	
	
	
	@Test
	public void testSimpleScoping() throws ScriptException{
		
		Scope s1 = new Scope();
		Scope s2 = new Scope(); s1.support.injectScriptSupport(s2, true);
		Scope s3 = new Scope(); s2.support.injectScriptSupport(s3, true);
		
		
		s1.support.bindValue("source", "scope1", false);
		s2.support.bindValue("source", "scope2", false);
		s3.support.bindValue("source", "scope3", false);
		
		assertEquals("scope3", s3.support.evaluate("source", false));
		assertEquals("scope2", s3.support.evaluate("parent.source", false));
		assertEquals("scope1", s3.support.evaluate("parent.parent.source", false));
		
	}
	
	@Test
	public void testGlobals() throws ScriptException{
		Scope s1 = new Scope();
		Scope s2 = new Scope(); s1.support.injectScriptSupport(s2, true);
		Scope s3 = new Scope(); s2.support.injectScriptSupport(s3, true);
		
		
		s1.support.bindValue("global", "global1", true);
		
		assertEquals("global1", s3.support.evaluate("global", false));
		s3.support.evaluate("global = 'global3'", false);
		
		assertEquals("global3", s1.support.evaluate("global", false));
	}
	
	
	@Test
	public void testHidingAndUnbind() throws ScriptException{
		Scope s1 = new Scope();
		Scope s2 = new Scope(); s1.support.injectScriptSupport(s2, true);
		Scope s3 = new Scope(); s2.support.injectScriptSupport(s3, true);
		
		
		s1.support.bindValue("global", "global1", true);
		s3.support.bindValue("global","global3", false);
		
		assertEquals("global3", s3.support.evaluate("global", false));
		
		s3.support.unbindValue("global", false);
		
		assertEquals("global1", s3.support.evaluate("global", false));
	}
	
}
