package services;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import models.Tag;
import play.db.ebean.Transactional;

/**
 *
 * @author eriklark
 */
public class TagsService {

	public static Tag findByName(String name) throws NoSuchElementException {
		Tag found = Tag.find.where().ieq("name", name).findUnique();
		if (found == null) {
			throw new NoSuchElementException("No tag with name " + name + " found");
		} else {
			return found;
		}
	}

	public static Set<String> getTagNames(Iterable<Tag> tags) {
		Set<String> tagNames = new HashSet<>();
		for(Tag tag : tags) {
			tagNames.add(tag.name);
		}

		return tagNames;
	}

	@Transactional
	public static Set<Tag> findOrCreateTags(Iterable<Tag> tagsToFindOrCreate) {
		Set<Tag> tags = new HashSet<>();
		for (Tag tag : tagsToFindOrCreate) {
			tags.add(findOrCreateTag(tag));
		}
		return tags;
	}

	private static Tag findOrCreateTag(Tag tag) {
		if (tag.id == null) {
			return TagNameService.findOrCreateTagFromName(tag.name);
		} else {
			return Tag.find.byId(tag.id);
		}
	}

	public static boolean hasOnlyEmptyNames(Iterable<Tag> tags) {
		for(Tag tag : tags) {
			if (!(tag.name == null || tag.name.trim().isEmpty())) {
				return false;
			}
		}
		return true;
	}

    public static void updateName(Tag tag, String newName) {
        tag.name = newName;
        tag.save();
    }
}
