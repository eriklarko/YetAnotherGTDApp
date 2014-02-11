package util;

/**
 *
 * @author eriklark
 */
public class ParsedPair<Key, Value> {

	private final Key key;
	private final Value value;

	public ParsedPair(Key key, Value value) {
		this.key = key;
		this.value = value;
	}

	public Key getKey() {
		return key;
	}

	public Value getValue() {
		return value;
	}
}
