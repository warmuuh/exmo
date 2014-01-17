package wrm.exmo.transform.xml;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import lombok.Data;

import org.junit.Test;

import wrm.exmo.transform.FluentMapper;
import wrm.exmo.transform.xml.XmlFluentMapper;

public class XmlMapperRecursiveTest {
	
	
	
	public  static @Data class SourceClass {
		String a;
		SourceClass child;
	}
	
	public static @Data class TargetClass {
		String a1;
		TargetClass child1;
	}

	
	@Test
	public void testRecursiveMapping() throws Exception {
		
		
		SourceClass sc = new SourceClass();
		sc.setA("lvl1");
		sc.setChild(new SourceClass());
		sc.getChild().setA("lvl2");
		
		
		
		FluentMapper<SourceClass, TargetClass> mapper = new XmlFluentMapper<SourceClass, TargetClass>(getClass().getResource("/data/testRecursiveMapping.xml"));
		
		TargetClass mappedObj = mapper.mapObject(sc, TargetClass.class);
		
		
		assertThat(mappedObj.a1, is("lvl1"));
		assertNotNull(mappedObj.child1);
		assertThat(mappedObj.getChild1().a1, is("lvl2"));
		
	}
	
	
	
}
