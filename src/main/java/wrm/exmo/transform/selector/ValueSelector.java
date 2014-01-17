package wrm.exmo.transform.selector;


public class ValueSelector implements Selector {

	private Object defaultValue;

	public ValueSelector(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
 
	@Override
	public Object getValue(Object source, Object target) {
		return defaultValue;
	}

	@Override
	public Class<?> getType(Object source, Object target) {
		return defaultValue.getClass();
	}

}
