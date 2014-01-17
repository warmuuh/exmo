package wrm.exmo.transform.converter;

public class ConversionException extends Exception {

	private static final long serialVersionUID = 1L;

	public ConversionException() {
		super();
	}

	public ConversionException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public ConversionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ConversionException(String arg0) {
		super(arg0);
	}

	public ConversionException(Throwable arg0) {
		super(arg0);
	}

}
