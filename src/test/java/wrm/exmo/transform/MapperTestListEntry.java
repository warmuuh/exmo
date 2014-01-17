package wrm.exmo.transform;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;

import org.junit.Test;

import wrm.exmo.transform.FluentMapper;

public class MapperTestListEntry {

	
	
	
	FluentMapper<ABeanClass, BBeanClass> mapper = new FluentMapper<MapperTestListEntry.ABeanClass, MapperTestListEntry.BBeanClass>(){{
		map("name").to("list1[0]");
	}};
	
	FluentMapper<ABeanClass, BBeanClass> mapperRec = new FluentMapper<MapperTestListEntry.ABeanClass, MapperTestListEntry.BBeanClass>(){{
		map("name").to("recList[0].list1[0]");
	}};
	
	public  static @Data class ABeanClass {
		String name; 
	}
	
	public static @Data class BBeanClass {
		List<String> list1;
		List<BBeanClass> recList;
	}
	
	
	
	@Test
	public void testSimple() throws Exception {
		
		ABeanClass a = new ABeanClass();
		a.setName("a");
		
		BBeanClass b = mapper.mapObject(a, BBeanClass.class);
		assertThat(b.getList1().get(0), is("a"));
	
	}


	@Test(expected=Exception.class)
	public void testSimpleRec() throws Exception {
		ABeanClass a = new ABeanClass();
		a.setName("a");
		BBeanClass b = mapperRec.mapObject(a, BBeanClass.class);
	}
}
