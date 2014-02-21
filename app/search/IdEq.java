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

	private final Long id;

	public IdEq(Long id) {
		this.id = id;
	}

    public Long getId() {
        return id;
    }

	@Override
	public Set<Note> execute() {
        Tag tag = Tag.find.byId(id);
        if (tag == null) {
            throw new IllegalArgumentException("No tag with id " + id + " found");
        }

		return NoteService.findNotesWithTag(tag);
	}

    @Override
    public String toString() {
        return "tag.id == " + id;
    }
}
