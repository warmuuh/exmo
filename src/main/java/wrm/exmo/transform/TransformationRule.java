package wrm.exmo.transform;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import wrm.exmo.transform.callback.RuleCallback;
import wrm.exmo.transform.converter.TypeConverter;
import wrm.exmo.transform.selector.Selector;


@Data
@SuppressWarnings("rawtypes")
public class TransformationRule {
	private List<TypeConverter> converters = new LinkedList<TypeConverter>();
	private Selector source;
	private String destination;
	private RuleCallback callback;
	private boolean deepMapping = false;
	private Class deepMappingType;
	private Selector collectionFactory;
}