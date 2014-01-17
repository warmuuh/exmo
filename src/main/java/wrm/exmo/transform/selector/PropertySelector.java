package wrm.exmo.transform.selector;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;



public class PropertySelector implements Selector {
	String name;

	public PropertySelector(String name) {
		super();
		this.name = name;
	}

	@Override
	public Object getValue(Object source, Object target) throws Exception {
			try {
				return PropertyUtils.getNestedProperty(source, name);
			} catch (NestedNullException e) {
				return null; //one sub-property was null... just return null as whole result
			}
	}

	@Override
	public Class<?> getType(Object source, Object target) throws Exception {
		try {
			return PropertyUtils.getPropertyType(source, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}