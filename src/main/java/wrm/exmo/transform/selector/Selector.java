package wrm.exmo.transform.selector;

public interface Selector {
	public Object getValue(Object source, Object target) throws Exception; 

	/**
	 * get the type of the object.
	 * Can be null, then the type of the value will be used instead
	 * 
	 */
	public Class<?> getType(Object source, Object target) throws Exception;
}