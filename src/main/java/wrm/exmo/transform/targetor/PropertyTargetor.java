package wrm.exmo.transform.targetor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.expression.DefaultResolver;

public class PropertyTargetor implements Targetor {

	private DefaultResolver resolver = new DefaultResolver();

	@Override
	public void  createPathToProperty(Object target, String destination) throws Exception {
		
		List<String> segments = new LinkedList<>();
		do{
			String cur = resolver.next(destination);
			destination= resolver.remove(destination);
			segments.add(cur);
		}while(destination != null);
		
		
		
		for (Iterator<String> iterator = segments.iterator(); iterator.hasNext();) {
			String seg = iterator.next();
			
			if (resolver.isIndexed(seg))
				target = createListProperty(target, seg, !iterator.hasNext());
			else if (resolver.isMapped(seg))
				target = createMapProperty(target, seg, !iterator.hasNext());
			else if (iterator.hasNext()) //create PATH to property (not property itself)
				target = createSimpleProperty(target, seg);
		}
		
		
		
	}

	private Object createListProperty(Object target, String seg, boolean isLast) throws Exception {
		String propName = resolver.getProperty(seg);
		
		Object curProperty = PropertyUtils.getNestedProperty(target, propName);
		if (curProperty == null)
		{
			if (!isLast)
				throw new Exception("Cannot create intermediate lists because of unknown element-type.");

			
			Class propertyType = PropertyUtils.getPropertyType(target, propName);
			if (propertyType.isAssignableFrom(ArrayList.class))
				curProperty = new ArrayList();
			else
				throw new Exception("Only interfaces of type List are supported for index-referencing");
			
			PropertyUtils.setNestedProperty(target, propName,  curProperty);
			//wrap for further processing:
			if (propertyType.isArray())
				curProperty = Arrays.asList(curProperty);
		}
		
		int idx = resolver.getIndex(seg);
		if (curProperty instanceof List)
		{
			List l = (List) curProperty;
			if (l.size() -1< idx) //not enough space in list
			{
				for(int i = l.size()-1; i < idx; ++i) //fill it with nulls
					l.add(null);
				return null; //return the new value, i.e. null
			}
			else
				return l.get(idx);
		}
		
		
		return PropertyUtils.getNestedProperty(target, seg);
		
	}

	private Object createMapProperty(Object target, String seg, boolean isLast) throws Exception {
		//TODO implement mapping based, does this even have to be implemented?!? for now, it does not because custom(CustomAttrib) works as is 
		if ( !isLast)
			throw new OperationNotSupportedException();
		
		return null;
	}

	private Object createSimpleProperty(Object target, String seg) throws Exception {
		Object curProperty = PropertyUtils.getNestedProperty(target, seg);
		if (curProperty == null)
		{
			Class propertyType = PropertyUtils.getPropertyType(target, seg);
			curProperty = propertyType.newInstance();
			PropertyUtils.setNestedProperty(target, seg,  curProperty);
		}
		return curProperty;
	}

	@Override
	public void setValue(Object target, String property, Object value) throws Exception {
		PropertyUtils.setProperty(target, property, value);
	}

	@Override
	public Class getPropertyType(Object target, String property) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return PropertyUtils.getPropertyType(target,property);
	}

}
