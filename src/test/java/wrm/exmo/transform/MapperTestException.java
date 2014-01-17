package wrm.exmo.transform;

import lombok.Data;

import org.junit.Before;
import org.junit.Test;

import wrm.exmo.test.SDDL;
import wrm.exmo.transform.FluentMapper;

public class MapperTestException {

	SDDL sddl;
	@Before
	public void setup(){
		sddl = new SDDL();
		sddl.registerClass(ABeanClass.class, "a");
		sddl.registerClass(BBeanClass.class, "b");
	}
	
	
	FluentMapper<ABeanClass, BBeanClass> mapper = new FluentMapper<MapperTestException.ABeanClass, MapperTestException.BBeanClass>(){{
		map("WRONG").to("s1");
		map("i").to("i1");
	}};
	
	public  static @Data class ABeanClass {
		String s;
		int i;
	}
	
	public static @Data class BBeanClass {
		String s1;
		int i1;
	}
	
	
	
	@Test(expected=Exception.class)
	public void testSimple() throws Exception {
		ABeanClass a = sddl.create("a { s:Hello, i:2}");
		BBeanClass b = 	mapper.mapObject(a, BBeanClass.class);
	
	
		
		
	}


}
