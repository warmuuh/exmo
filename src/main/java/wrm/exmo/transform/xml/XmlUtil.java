package wrm.exmo.transform.xml;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import javax.sql.rowset.spi.XmlReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import lombok.extern.apachecommons.CommonsLog;

import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import wrm.exmo.transform.xml.model.ObjectFactory;
import wrm.exmo.transform.xml.model.TMapper;


@CommonsLog
public class XmlUtil {

	public static Class<?> loadClass(String name) {
		if (name == null || name.isEmpty())
			return null;

		try {
			return Class.forName(name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String writeClass(Class<?> clazz) {
		if (clazz == null)
			return null;
		return clazz.getName();
	}

	/* package */static TMapper parseMapperXml(URL resource) {
		try {
			JAXBContext ctx = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller um = ctx.createUnmarshaller();
			um.setProperty("com.sun.xml.internal.bind.IDResolver", new MapperIDResolver());
				return (TMapper) um.unmarshal(createSource(resource));
				
		} catch (Exception e) {
			log.error("Error parsing file", e);
			throw new RuntimeException(e); // kill program
		}
	}

	/* package */static SAXSource createSource(URL resource) throws Exception {
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		parserFactory.setNamespaceAware(true);
		SAXParser saxParser = parserFactory.newSAXParser();
		
		XMLReader xmlReader = saxParser.getXMLReader();
		InputSource inSrc = new InputSource(resource.openStream());
		EntityResolver resolver = new IncludeEntityResolver();
		xmlReader.setEntityResolver(resolver);
		
		SAXSource saxSource = new SAXSource(xmlReader, inSrc);
		
		return saxSource;
	}

}
