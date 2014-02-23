package services;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.persistence.PersistenceException;
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

	public Tag findByName(String name) throws NoSuchElementException {
		try {
			Tag found = find().ieq("name", name).findUnique();
			if (found == null) {
				throw new NoSuchElementException("No tag with name " + name + " found");
			} else {
				return found;
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
			it.next();

			while (it.hasNext()) {
				it.next().delete();
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

	public static Tag createNewTagFromName(String tagName) {
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
}
