package search;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import models.Note;

/**
 *
 * @author eriklark
 */
public class Or implements Node{

	private final Collection<Node> children;

	public Or(Collection<Node> children) {
        if (children.size() < 2) {
            throw new IllegalArgumentException("Or operations need at least two children");
        }
		this.children = children;
	}

    public Collection<Node> getChildren() {
        return new LinkedList<>(children);
    }

	@Override
	public Set<Note> execute() {
		Set<Note> notes = new HashSet<>();

        Iterator<Node> it = children.iterator();
		while(it.hasNext()) {
			notes.addAll(it.next().execute());
		}
		return notes;
	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");

        Iterator<Node> it = children.iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());

            if (it.hasNext()) {
                sb.append(" OR ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
