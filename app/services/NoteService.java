package services;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import models.Note;
import models.Tag;
import play.db.ebean.Transactional;
import utils.NotesIdComparator;

/**
 *
 * @author eriklark
 */
public class NoteService extends BaseService<Note> {

	private static final NoteService instance = new NoteService();

	public static NoteService instance() {
		return instance;
	}

	private NoteService() {
		super(Note.class);
	}

	@Transactional
	public Note create(String payload, Iterable<Tag> tags) {
		Note toCreate = new Note();
		toCreate.owner = UserService.getCurrentUser();
		toCreate.payload = payload;
		toCreate.tags = TagService.instance().findOrCreateTags(tags);
		toCreate.save();
		return toCreate;
	}

	@Transactional
	public void updatePayload(Note note, String payload) {
		note.payload = payload;
		note.save();
	}

	/**
	 * Removes a note. If there are tags which are no longer connected to
	 * any note, they are marked for deletion.
	 */
	@Transactional
	public void remove(Note note) {
		note.deleteManyToManyAssociations("tags");
		note.delete();
	}

	@Transactional
	public void removeTag(Note note, Tag tagToRemove) throws NoSuchElementException {
		boolean tagRemoved = note.tags.remove(tagToRemove);

		if (tagRemoved) {
			note.save();
		} else {
			throw new NoSuchElementException("Unable to find tag " + tagToRemove.name + " in note " + note.id);
		}
	}

	@Transactional
	public void addTag(Note note, Tag tag) {
		note.tags.add(tag);
		note.save();
	}

	public Set<Note> findNotesTaggedWith(Iterable<Tag> tags) {
		Set<Note> notes = new TreeSet<>(new NotesIdComparator());
		for (Tag tag : tags) {
			notes.addAll(findNotesWithTag(tag));
		}
		return notes;
	}

    public Set<Note> findNotesWithTag(Tag tag) {
		return find().eq("tags.name", tag.name).findSet();
	}

    public Set<Note> findAllCurrentUsersNotes() {
		return find().findSet();
	}
}
