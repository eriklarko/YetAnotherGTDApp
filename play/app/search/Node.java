package search;

import java.util.Set;
import models.Note;

/**
 *
 * @author eriklark
 */
public abstract class Node {

    private final Node parent;

    public Node(Node parent) {
        this.parent = parent;
    }

    public Node parent() {
        return parent;
    }

	public abstract Set<Note> execute();
}
