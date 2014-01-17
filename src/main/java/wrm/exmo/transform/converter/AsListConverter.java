package wrm.exmo.transform.converter;

import java.util.Arrays;
import java.util.List;


public  class AsListConverter<T> implements TypeConverter<T, List<T>> {

	@Override
	public List<T> convert(T source, Class<T> sourceValClass,
			Class<List<T>> targetValClass) {
		return Arrays.<T>asList(source);
	}

} 
