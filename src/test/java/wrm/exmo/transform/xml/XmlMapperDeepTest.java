package wrm.exmo.transform.xml;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.junit.Test;

import wrm.exmo.transform.FluentMapper;
import wrm.exmo.transform.xml.XmlFluentMapper;

public class XmlMapperDeepTest {

	
	public  static @Data class SourceClass {
		List<SubSourceClass> subList = new LinkedList<>();
	}
	
	public static @Data class TargetClass {
		List<SubTargetClass> subList1 = new LinkedList<>();
	}

	@AllArgsConstructor
	public  static @Data class SubSourceClass {
		String sub_a;
	}
	
	public  static @Data class SubTargetClass {
		String sub_a1;
	}
	
	@Test
	public void testDeepMapping() throws Exception {
		
		
		SourceClass sc = new SourceClass();
		sc.getSubList().add(new SubSourceClass("a"));
		
		FluentMapper<SourceClass, TargetClass> mapper = new XmlFluentMapper<SourceClass, TargetClass>(getClass().getResource("/data/testDeepMapping.xml"));
		
		TargetClass mappedObj = mapper.mapObject(sc, TargetClass.class);
		
		
		assertThat(mappedObj.getSubList1().size(), is(1));
		assertThat(mappedObj.getSubList1().get(0).sub_a1, is("a"));
		
	}
	
	
	
}
