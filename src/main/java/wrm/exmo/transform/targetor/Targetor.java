package wrm.exmo.transform.targetor;

public interface Targetor {
	public void createPathToProperty(Object target, String property) throws Exception;;
	public void setValue(Object target, String property, Object value) throws Exception;

	public Class getPropertyType(Object target, String property) throws Exception;;
}
