package util;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author eriklark
 */
public class Validation<T> {

	private final Map<String, Collection<T>> errors;
	private final Map<String, Collection<T>> parsed;

	public Validation() {
		errors = new HashMap<>();
		parsed = new HashMap<>();
	}

	public Map<String, Collection<T>> errors() {
		return new HashMap<>(errors);
	}

	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	public void reject(String field, T reason) {
		append(errors, field, reason);
	}

	private void append(Map<String, Collection<T>> map, String field, T toAppend) {
		Collection<T> current = map.get(field);
		if (current == null) {
			current = new LinkedList<>();
			map.put(field, current);
		}
		current.add(toAppend);
	}

	public void successfullyParsed(String field, T result) {
		append(parsed, field, result);
	}

	public Collection<T> getParsed(String key) {
		return parsed.get(key);
	}
}
