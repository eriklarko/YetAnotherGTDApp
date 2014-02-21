package search;

/**
 *
 * @author eriklark
 */
public class IdEq<T> implements Node<String> {

	private final String fieldName;
	private final T id;

	public IdEq(String fieldName, T id) {
		this.fieldName = fieldName;
		this.id = id;
	}

	@Override
	public String execute() {
		return fieldName + " = " + id;
	}

}
