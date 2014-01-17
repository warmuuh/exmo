package wrm.exmo.transform;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;

import org.junit.Test;

import wrm.exmo.transform.FluentMapper;
import wrm.exmo.transform.converter.TypeConverter;

public class MapperTestListConverter {

	
	
	
	FluentMapper<ABeanClass, BBeanClass> mapper = new FluentMapper<MapperTestListConverter.ABeanClass, MapperTestListConverter.BBeanClass>(){{
		map("list").to("list1").asElementsOfType(BBeanClass.class).with(new TypeConverter<String, String>() {
			@Override
			public String convert(String source, Class<String> sourceValClass,
					Class<String> targetValClass) {
				return source + "_converted";
			}
			
		});
	}};
	
	public  static @Data class ABeanClass {
		List<String> list; 
	}
	
	public static @Data class BBeanClass {
		List<String> list1;
	}
	
	
	
	@Test
	public void testSimple() throws Exception {
		ABeanClass a = new ABeanClass();
		a.setList(new LinkedList<String>());
		a.getList().add("ee");
		a.getList().add("aa");
		
		
		BBeanClass b = mapper.mapObject(a, BBeanClass.class);
	
		
		
		assertThat(b.getList1().get(0), is("ee_converted"));
		assertThat(b.getList1().get(1), is("aa_converted"));
	
	}


}
