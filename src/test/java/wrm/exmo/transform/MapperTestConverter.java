package wrm.exmo.transform;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import lombok.Data;

import org.junit.Before;
import org.junit.Test;

import wrm.exmo.test.SDDL;
import wrm.exmo.transform.FluentMapper;
import wrm.exmo.transform.converter.TypeConverter;

public class MapperTestConverter {

	SDDL sddl;
	@Before
	public void setup(){
		sddl = new SDDL();
		sddl.registerClass(ABeanClass.class, "a");
		sddl.registerClass(BBeanClass.class, "b");
	}
	
	
	FluentMapper<ABeanClass, BBeanClass> mapper = new FluentMapper<MapperTestConverter.ABeanClass, MapperTestConverter.BBeanClass>(){{
		map("s").to("s1").with(new TypeConverter<String, String>() {

			@Override
			public String convert(String source, Class<String> sourceValClass,
					Class<String> targetValClass) {
				return source + "_converted";
			}
			
		});
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
	
	
	
	@Test
	public void testSimple() throws Exception {
		ABeanClass a = sddl.create("a { s:Hello, i:2}");
		BBeanClass b = mapper.mapObject(a, BBeanClass.class);
	
		
		
		assertThat(b.s1, is("Hello_converted"));
		assertThat(b.i1, is(2));
	
	}


	@Test
	public void testNull() throws Exception {
		ABeanClass a = sddl.create("a { i:2}");
		BBeanClass b = mapper.mapObject(a, BBeanClass.class);
	
		
		
		assertNull(b.s1);
		assertThat(b.i1, is(2));
	}
}
