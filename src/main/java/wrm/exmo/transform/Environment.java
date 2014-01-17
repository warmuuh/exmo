package wrm.exmo.transform;

import java.util.List;

/**
 * The environment is used during mapping to access environment variables etc.
 * E.g. in scripts, you can access them using $['MyVar']
 * @author pemucha
 *
 */
public interface Environment {
	public Object resolveVariable(String name);
	public List<String> getRegisteredVariableNames();
}
