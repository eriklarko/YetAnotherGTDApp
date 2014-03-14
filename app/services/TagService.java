package services;

import java.util.Collection;
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
					NoteService.instance().addTag(note, keep);
					NoteService.instance().removeTag(note, toDelete);
				}

				toDelete.delete();
			}
		}
	}

	public Set<String> getTagNames(Iterable<Tag> tags) {
		Set<String> tagNames = new HashSet<>();
		for (Tag tag : tags) {
			tagNames.add(tag.name);
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
			return TagNameService.findOrCreateTagFromName(tag.name);
		} else {
			return byId(tag.id);
		}
	}

	public boolean hasOnlyEmptyNames(Iterable<Tag> tags) {
		for (Tag tag : tags) {
			if (!(tag.name == null || tag.name.trim().isEmpty())) {
				return false;
			}
		}
		return true;
	}

	@Transactional
	public Tag createNewTagFromName(String tagName) {
		Tag tag = new Tag();
		tag.owner = UserService.getCurrentUser();
		tag.name = tagName;
		tag.save();
		return tag;
	}

	public void updateName(Tag tag, String newName) {
		tag.name = newName;
		tag.save();
	}

	public Set<Tag> findTagsWithNameMatchingQuery(String query) {
		return find().like("name", query.toLowerCase()).findSet();
	}
}
