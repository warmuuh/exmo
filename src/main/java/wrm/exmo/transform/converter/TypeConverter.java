package wrm.exmo.transform.converter;

public interface TypeConverter<CS, CT> {
	public CT convert(CS source, Class<CS> sourceValClass,
			Class<CT> targetValClass) throws ConversionException;
}