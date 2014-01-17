package wrm.exmo.transform.xml;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import javax.xml.datatype.XMLGregorianCalendar;

import lombok.Data;

import org.junit.Test;

import wrm.exmo.transform.FluentMapper;
import wrm.exmo.transform.xml.XmlFluentMapper;

public class XmlMapperScriptTest {

	
	
	public  static @Data class SourceClass {
		String a;
		String b;
		XMLGregorianCalendar date;
	}
	
	public static @Data class TargetClass {
		String a1;
		String b1;
		String b1x;
	}

	
	
	@Test
	public void testScriptFunctionMapping() throws Exception {
		
		
		SourceClass sc = new SourceClass(){{a="a"; b="b";}};
	
		
		FluentMapper<SourceClass, TargetClass> mapper = new XmlFluentMapper<SourceClass, TargetClass>(getClass().getResource("/data/testScript.xml"));
		
		TargetClass mappedObj = mapper.mapObject(sc, TargetClass.class);
		
		
		assertThat(mappedObj.a1, equalTo("a_converted"));
		assertThat(mappedObj.b1, equalTo("initializedValue"));
		
		
		
		
	}
	
	
	
}
