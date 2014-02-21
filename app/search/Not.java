package search;

import java.util.Set;
import models.Note;
import services.NoteService;

/**
 *
 * @author Thinner
 */
public class Not implements Node {

    private final Node child;

    public Not(Node child) {
        this.child = child;
    }

    public Node getChild() {
        return child;
    }

	@Override
	public Set<Note> execute() {
        Set<Note> universe = NoteService.findAllTagsOwnedBy(Long.MIN_VALUE);
        Set<Note> notesToRemove = child.execute();
        universe.removeAll(notesToRemove);

        return universe;
    }

    @Override
    public String toString() {
        return "NOT (" + child.toString() + ")";
    }
}
