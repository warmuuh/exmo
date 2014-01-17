package wrm.exmo.transform.selector;

public class ContextSelector implements Selector {

	private Selector selector;
	private Object context;

	public ContextSelector(Object context, Selector selector) {
		this.context = context;
		this.selector = selector;
	}

	@Override
	public Object getValue(Object source, Object target) throws Exception {
		return selector.getValue(context, target);
	}

	@Override
	public Class<?> getType(Object source, Object target) throws Exception {
		return selector.getType(context, target);
	}

}
