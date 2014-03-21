package services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import removewhenjava8.Optional;
import java.util.Set;
import javax.persistence.PersistenceException;
import models.Note;
import models.Tag;
import play.db.ebean.Transactional;

/**
 *
 * @author eriklark
 */
public class TagService extends BaseService<Tag> {

	private static final TagService instance = new TagService();

	public static TagService instance() {
		return instance;
	}

	private TagService() {
		super(Tag.class);
	}

	public Set<Tag> getAllCurrentUsersTags() {
		return find().findSet();
	}

	public Optional<Tag> findByName(String name) {
		try {
			Tag found = find().ieq("name", name).findUnique();
			if (found == null) {
				return Optional.empty();
			} else {
				return Optional.of(found);
			}
		} catch (PersistenceException ex) {
			System.out.println("There was a problem finding tag >" +name+ "<");
			findAndRemoveDuplicates(name);

			return findByName(name);
		}
	}

	private void findAndRemoveDuplicates(String name) {
		List<Tag> tagsWithConflictingNames = find().ieq("name", name).findList();
		Iterator<Tag> it = tagsWithConflictingNames.iterator();
		if (it.hasNext()) {
			Tag keep = it.next();

			while (it.hasNext()) {
				Tag toDelete = it.next();
				Iterable<Note> notes = NoteService.instance().findNotesWithTag(toDelete);
				for (Note note : notes) {
					System.out.println("Note " + note.id + " has the tag to remove. " + Arrays.deepToString(note.tags.toArray()));

					NoteService.instance().addTag(note, keep);
					NoteService.instance().removeTag(note, toDelete);
				}

				// TODO Find all filters referencing the tag to remove and update them
				//FilterService.instance().

				toDelete.delete();
			}
		}
	}

	public Set<String> getTagNames(Iterable<Tag> tags) {
		Set<String> tagNames = new HashSet<>();
		for (Tag tag : tags) {
			tagNames.add(tag.getName());
		}

		return tagNames;
	}

	@Transactional
	public Set<Tag> findOrCreateTags(Iterable<Tag> tagsToFindOrCreate) {
		Set<Tag> tags = new HashSet<>();
		for (Tag tag : tagsToFindOrCreate) {
			tags.add(findOrCreateTag(tag));
		}
		return tags;
	}

	private Tag findOrCreateTag(Tag tag) {
		if (tag.id == null) {
			return TagNameService.findOrCreateTagFromName(tag.getName());
		} else {
			return byId(tag.id);
		}
	}

	public boolean hasOnlyEmptyNames(Iterable<Tag> tags) {
		for (Tag tag : tags) {
			if (!(tag.getName() == null || tag.getName().trim().isEmpty())) {
				return false;
			}
		}
		return true;
	}

	@Transactional
	public Tag createNewTagFromName(String tagName) {
		Tag tag = new Tag();
		tag.owner = UserService.getCurrentUser();
		tag.setName(tagName);
		tag.save();
		return tag;
	}

	public Tag updateName(Tag tag, String newName) {
		tag.setName(newName);
		tag.save();

		return tag;
	}

	public Set<Tag> findTagsWithNameMatchingQuery(String query) {
		return find().ilike("name", query).findSet();
	}

	public Tag getArchiveTag() {
		return TagNameService.findOrCreateTagFromName("archive");
	}
}
