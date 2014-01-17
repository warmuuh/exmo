package wrm.exmo.transform;

import static org.junit.Assert.*;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import lombok.Data;

import org.junit.Test;

import wrm.exmo.transform.FluentMapper;
import wrm.exmo.transform.converter.TypeConverter;

public class MapperTestConverterInterface {

	
	
	
	FluentMapper<ABeanClass, BBeanClass> mapper = new FluentMapper<MapperTestConverterInterface.ABeanClass, MapperTestConverterInterface.BBeanClass>(){{
		registerGlobalConverter(new TypeConverter<Date, XMLGregorianCalendar>() {

			@Override
			public XMLGregorianCalendar convert(Date source, Class<Date> sourceValClass,
					Class<XMLGregorianCalendar> targetValClass) {
				return new com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl();
			}
			
		}, Date.class, XMLGregorianCalendar.class);
		map("date").to("cal");
	}};
	
	
	FluentMapper<BBeanClass, ABeanClass> mapperRev = new FluentMapper<MapperTestConverterInterface.BBeanClass, MapperTestConverterInterface.ABeanClass>(){{
		registerGlobalConverter(new TypeConverter<XMLGregorianCalendar, Date >() {

			@Override
			public Date convert(XMLGregorianCalendar source, Class<XMLGregorianCalendar> sourceValClass,
					Class<Date> targetValClass) {
				return new Date();
			}
			
		}, XMLGregorianCalendar.class, Date.class);
		map("cal").to("date");
	}};
	
	
	public  static @Data class ABeanClass {
		Date date;
	}
	
	public static @Data class BBeanClass {
		XMLGregorianCalendar cal;
	}
	
	
	
	@Test
	public void testDateToCalendar() throws Exception {
		ABeanClass a = new ABeanClass();
		a.setDate(new Date());
		BBeanClass b = mapper.mapObject(a, BBeanClass.class);
	
		assertNotNull(b.getCal());
	}
	
	
	@Test
	public void testCalendarToDate() throws Exception {
		BBeanClass b = new BBeanClass();
		b.setCal( new com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl());
		ABeanClass a = mapperRev.mapObject(b, ABeanClass.class);
	
		assertNotNull(a.getDate());
	}
}
