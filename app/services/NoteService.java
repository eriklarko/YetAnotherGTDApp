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
public class NoteService {

	@Transactional
	public static Note create(String payload, Iterable<Tag> tags) {
		Note toCreate = new Note();
		toCreate.payload = payload;
		toCreate.tags = TagsService.findOrCreateTags(tags);
		toCreate.save();
		return toCreate;
	}

	@Transactional
	public static void updatePayload(Note note, String payload) {
		note.payload = payload;
		note.save();
	}

	/**
	 * Removes a note. If there are tags which are no longer connected to
	 * any note, they are marked for deletion.
	 */
	@Transactional
	public static void remove(Note note) {
		Set<Tag> tags = note.tags;
		note.deleteManyToManyAssociations("tags");
		note.delete();
	}

	@Transactional
	public static void removeTag(Note note, Tag tagToRemove) throws NoSuchElementException {
		boolean tagRemoved = note.tags.remove(tagToRemove);

		if (tagRemoved) {
			note.save();
		} else {
			throw new NoSuchElementException("Unable to find tag " + tagToRemove.name + " in note " + note.id);
		}
	}

	@Transactional
	public static void addTag(Note note, Tag tag) {
		note.tags.add(tag);
		note.save();
	}

	public static Set<Note> findNotesTaggedWith(Iterable<Tag> tags) {
		Set<Note> notes = new TreeSet<>(new NotesIdComparator());
		for (Tag tag : tags) {
			notes.addAll(findNotesWithTag(tag));
		}
		return notes;
	}

    public static Set<Note> findNotesWithTag(Tag tag) {
		return Note.find.where().eq("tags.name", tag.name).findSet();
	}
}
