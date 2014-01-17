package wrm.exmo.transform.xml;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import lombok.Data;

import org.junit.Test;

import wrm.exmo.transform.FluentMapper;
import wrm.exmo.transform.xml.XmlFluentMapper;

public class XmlMapperIncludeTest {

	
	
	public  static @Data class SourceClass {
		String a;
	}
	
	public static @Data class TargetClass {
		String a1;
	}

	
	@Test
	public void testIncludeMapping() throws Exception {
		
		
		SourceClass sc = new SourceClass();
		sc.setA("a");
		
		FluentMapper<SourceClass, TargetClass> mapper = new XmlFluentMapper<SourceClass, TargetClass>(getClass().getResource("/data/testInclude.xml"));
		
		TargetClass mappedObj = mapper.mapObject(sc, TargetClass.class);
		
		
		assertThat(mappedObj.getA1(), is("a_fromIncluded"));
		
	}
	
	
	
}
