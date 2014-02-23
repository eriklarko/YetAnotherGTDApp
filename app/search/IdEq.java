package search;

import java.util.Set;
import models.Note;
import models.Tag;
import services.NoteService;

/**
 *
 * @author eriklark
 */
public class IdEq implements Node {

	private final Tag tag;

	public IdEq(Tag tag) {
		this.tag = tag;
	}

    public Tag getTag() {
        return tag;
    }

	@Override
	public Set<Note> execute() {
        return NoteService.instance().findNotesWithTag(tag);
	}

    @Override
    public String toString() {
        return "tag.id == " + tag;
    }
}
