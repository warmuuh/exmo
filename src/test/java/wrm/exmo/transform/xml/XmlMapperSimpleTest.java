package wrm.exmo.transform.xml;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import lombok.Data;

import org.junit.Test;

import wrm.exmo.transform.FluentMapper;
import wrm.exmo.transform.xml.XmlFluentMapper;

public class XmlMapperSimpleTest {
	
	
	public  static @Data class SourceClass {
		String a;
		String b;
		XMLGregorianCalendar date;
		SubSourceClass suba = new SubSourceClass();
		SubSourceClass subb = new SubSourceClass();
	}
	
	public static @Data class TargetClass {
		String a1;
		String b1;
		String b1x;
		String date1;
		SubTargetClass suba1;
		SubTargetClass subb1;
	}

	public  static @Data class SubSourceClass {
		String sub_a;
	}
	
	public  static @Data class SubTargetClass {
		String sub_a1;
	}
	
	@Test
	public void testSimpleMapping() throws Exception {
		
		SourceClass sc = new SourceClass(){{a="a"; b="b";}};
	
		
		FluentMapper<SourceClass, TargetClass> mapper = new XmlFluentMapper<SourceClass, TargetClass>(getClass().getResource("/data/testSimpleMapping.xml"));
		
		TargetClass mappedObj = mapper.mapObject(sc, TargetClass.class);
		
		
		assertThat(mappedObj.a1, equalTo("a"));
		assertThat(mappedObj.b1, equalTo("b"));
		assertThat(mappedObj.b1x, equalTo("X"));
		
		
		
		
	}
	
	
	
	@Test
	public void testConverter() throws Exception {

		
		SimpleDateFormat fmt = new SimpleDateFormat("YYYYMMdd");
		
		
		SourceClass sc = new SourceClass(){{a="a"; date=getXMLGregorianCalendarNow();}};
		sc.suba.sub_a = "subexpr1";
		sc.subb.sub_a = "subexpr2";
		
		FluentMapper<SourceClass, TargetClass> mapper = new XmlFluentMapper<SourceClass, TargetClass>(getClass().getResource("/data/testConverter.xml"));
		TargetClass mappedObj = mapper.mapObject(sc, TargetClass.class);
		assertThat(mappedObj.a1, equalTo("a_converted"));
		assertThat(mappedObj.date1, equalTo(fmt.format(new Date())));
		assertThat(mappedObj.suba1.sub_a1, equalTo("subexpr1"));
		assertThat(mappedObj.subb1.sub_a1, equalTo("subexpr2"));
	}
	
	
	
	
	
	public XMLGregorianCalendar getXMLGregorianCalendarNow() 
            throws DatatypeConfigurationException
    {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar now = 
            datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
        return now;
    }

}
