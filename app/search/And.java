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
public class And implements Node {

    private final Collection<Node> children;

    public And(Collection<Node> children) {
        this.children = children;
    }

    public Collection<Node> getChildren() {
        return new LinkedList<>(children);
    }

    @Override
    public Set<Note> execute() {
        Iterator<Node> it = children.iterator();
        Set<Note> notes = new HashSet<>(it.next().execute());

        while (it.hasNext()) {
            notes.retainAll(it.next().execute());
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
                sb.append(" AND ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
