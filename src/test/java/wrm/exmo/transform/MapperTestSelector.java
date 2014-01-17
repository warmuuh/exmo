package wrm.exmo.transform;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import lombok.Data;

import org.junit.Before;
import org.junit.Test;

import wrm.exmo.test.SDDL;
import wrm.exmo.transform.FluentMapper;

public class MapperTestSelector {

	SDDL sddl;
	@Before
	public void setup(){
		sddl = new SDDL();
		sddl.registerClass(ABeanClass.class, "a");
		sddl.registerClass(BBeanClass.class, "b");
	}
	
	
	FluentMapper<ABeanClass, BBeanClass> mapper = new FluentMapper<MapperTestSelector.ABeanClass, MapperTestSelector.BBeanClass>(){{
		map(expr("source.s")).to("s1");
		map(value(5)).to("i1");
		map("o").to("o1").onNull(value("empty"));
	}};
	
	public  static @Data class ABeanClass {
		String s;
		int i;
		Object o;
	}
	
	public static @Data class BBeanClass {
		String s1;
		int i1;
		Object o1;
	}
	
	
	
	@Test
	public void testSimple() throws Exception {
		ABeanClass a = sddl.create("a { s:Hello, i:2}");
		a.setO("obj");
		BBeanClass b = mapper.mapObject(a, BBeanClass.class);
	
		
		
		assertThat(b.s1, is("Hello"));
		assertThat(b.i1, is(5)); //constant mapping
		assertThat(b.o1.toString(), is("obj"));
	}


	@Test
	public void testNull() throws Exception {
		ABeanClass a = sddl.create("a { i:2}");
		BBeanClass b = mapper.mapObject(a, BBeanClass.class);
	
		
		
		assertNull(b.s1);
		assertThat(b.i1, is(5)); //constant mapping
		assertThat(b.o1.toString(), is("empty")); //because of onNull selector
	}
}
