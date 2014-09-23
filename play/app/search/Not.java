package search;

import java.util.Set;
import models.Note;
import services.NoteService;

/**
 *
 * @author Thinner
 */
public class Not extends Node {

    private Node child;

    public Not(Node parent) {
        super(parent);
    }

    public Not(Node parent, Node child) {
        super(parent);
        this.child = child;
    }

    public Node getChild() {
        return child;
    }

    public void setChild(Node child) {
        this.child = child;
    }

	@Override
	public Set<Note> execute() {
        Set<Note> universe = NoteService.instance().findAllCurrentUsersNotes();
        Set<Note> notesToRemove = child.execute();
        universe.removeAll(notesToRemove);

        return universe;
    }

    @Override
    public String toString() {
        return "NOT (" + String.valueOf(child) + ")";
    }
}
