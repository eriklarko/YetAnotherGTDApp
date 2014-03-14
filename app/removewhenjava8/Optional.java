package removewhenjava8;

/**
 *
 * @author eriklark
 */
public class Optional<T> {

	public static <T> Optional<T> of(T t) {
		return new Optional<>(t);
	}

	public static <T> Optional<T> empty() {
		return new Optional<>(null);
	}

	private final T t;

	private Optional(T t) {
		this.t = t;
	}

	public T get() {
		return t;
	}

	public boolean isPresent() {
		return t != null;
	}
}
