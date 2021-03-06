package wrm.exmo.transform;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import lombok.Data;

import org.junit.Before;
import org.junit.Test;

import wrm.exmo.test.SDDL;
import wrm.exmo.transform.FluentMapper;
import wrm.exmo.transform.callback.RuleCallback;

public class MapperTestCallback {

	SDDL sddl;
	@Before
	public void setup(){
		sddl = new SDDL();
		sddl.registerClass(ABeanClass.class, "a");
		sddl.registerClass(BBeanClass.class, "b");
	}
	
	
	FluentMapper<ABeanClass, BBeanClass> mapper = new FluentMapper<MapperTestCallback.ABeanClass, MapperTestCallback.BBeanClass>(){
	@Override protected void createRules() {
		map("s").to("s1").andDo("target.s1 = 'byCallback'");
		map("i").to("i1").andDo(new RuleCallback() {
			
			@Override
			public void execute(Object source, Object target) {
				((BBeanClass)target).i1 = 99;
			}
		});
		
	}
	};
	
	
	public  static @Data class ABeanClass {
		String s;
		int i;
	}
	
	public static @Data class BBeanClass {
		String s1;
		int i1;
	}
	
	
	
	@Test
	public void testSimple() throws Exception {
		ABeanClass a = sddl.create("a { s:Hello, i:2}");
		BBeanClass b = 	mapper.mapObject(a, BBeanClass.class);
	
	
		
		assertThat(b.s1, is("byCallback"));
		assertThat(b.i1, is(99));
	
	}


	@Test
	public void testNull() throws Exception {
		ABeanClass a = sddl.create("a { i:2}");
		BBeanClass b = 	mapper.mapObject(a, BBeanClass.class);
	
	
		
		
		assertNull(b.s1); //callbackshould not be called
		assertThat(b.i1, is(99));
	}
}
