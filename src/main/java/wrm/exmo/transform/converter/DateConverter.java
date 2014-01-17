package wrm.exmo.transform.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

public class DateConverter implements
		TypeConverter<XMLGregorianCalendar, String> {

	String format;
	
	
	
	public DateConverter(String format) {
		super();
		this.format = format;
	}



	@Override
	public String convert(XMLGregorianCalendar source,
			Class<XMLGregorianCalendar> sourceValClass,
			Class<String> targetValClass) {
		
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date time = source.toGregorianCalendar().getTime();
		
		return formatter.format(time);
	}

}