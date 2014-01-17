package wrm.exmo.transform.xml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.xml.bind.ValidationEventHandler;

import org.xml.sax.SAXException;

import wrm.exmo.transform.xml.model.TMapper;


public class MapperIDResolver extends com.sun.xml.internal.bind.IDResolver{

	 Map<String,TMapper> mappers = new HashMap<String,TMapper>();
	
	 Set<String> unresolved = new HashSet<>();
	 
	 @Override
	public void startDocument(ValidationEventHandler eventHandler)
			throws SAXException {
		super.startDocument(eventHandler);
		mappers.clear();
	}
	
	@Override
	public void bind(String id, Object obj) throws SAXException {
		if (obj instanceof TMapper){
			if (mappers.containsKey(id))
				throw new SAXException("Duplicate ID: " + id);
			mappers.put(id.toString(), (TMapper) obj);
		
		}
	}

	@Override
	public Callable<TMapper> resolve(final String id, final Class targetType) throws SAXException {
		return new Callable<TMapper>() {
			
			@Override
			public TMapper call() throws Exception {
				if (targetType != TMapper.class)
					return null;
				
				TMapper mapper = mappers.get(id);
				if (mapper == null)
					unresolved.add(id.toString());
				else
					unresolved.remove(id);
				
				return mapper;
			}
		} ;
	}

	
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		
		if (unresolved.size() > 0)
			throw new SAXException("Unresolved references: " + unresolved);
	}


}
