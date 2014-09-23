package services;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;
import models.Note;
import models.Tag;
import play.db.ebean.Transactional;

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
		toCreate.saveManyToManyAssociations("tags");
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
			throw new NoSuchElementException("Unable to find tag " + tagToRemove.getName() + " in note " + note.id);
		}
	}

	@Transactional
	public void replaceTags(Note note, Collection<Tag> tags) {
		note.tags.clear();
		note.deleteManyToManyAssociations("tags");

		note.tags.addAll(tags);
		note.save();
		note.saveManyToManyAssociations("tags");
	}

	@Transactional
	public void addTag(Note note, Tag tag) {
		note.tags.add(tag);
		note.save();
	}

    public Set<Note> findNotesWithTag(Tag tag) {
		return find().eq("tags.id", tag.id).findSet();
	}

    public Set<Note> findAllCurrentUsersNotes() {
		return find().findSet();
	}

	public Set<Note> findNotesWithPayloadMatchingQuery(String query) {
		return find().ilike("payload", query).findSet();
	}
}
