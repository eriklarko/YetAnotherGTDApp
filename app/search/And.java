package search;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author eriklark
 */
public class And implements Node<String>{

	private final Collection<Node<String>> children;

	public And(Collection<Node<String>> children) {
		this.children = children;
	}

	@Override
	public String execute() {
		StringBuilder sb = new StringBuilder("(");

		Iterator<Node<String>> it = children.iterator();
		while(it.hasNext()) {
			sb.append(it.next().execute());
			if (it.hasNext()) {
				sb.append(" AND ");
			}
		}
		sb.append(")");
		return sb.toString();
	}

}
