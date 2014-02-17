package services;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import models.Tag;
import play.db.ebean.Transactional;
import static services.TagsService.findByName;

/**
 *
 * @author eriklark
 */
public class TagNameService {

	@Transactional
	public static Set<Tag> findOrCreateTagsFromNames(Iterable<String> tagNames) {
		Set<Tag> tags = new HashSet<>();
		for (String tagName : tagNames) {
			tags.add(findOrCreateTagFromName(tagName));
		}
		return tags;
	}

	public static Tag findOrCreateTagFromName(String tagName) {
		try {
			return findByName(tagName);
		} catch (NoSuchElementException ex) {
			return createNewTagFromName(tagName);
		}
	}

	private static Tag createNewTagFromName(String tagName) {
		Tag tag = new Tag();
		tag.name = tagName;
		tag.save();
		return tag;
	}

	public static Set<Tag> findTagsByName(Iterable<String> tagNames) throws NoSuchElementException {
		Set<Tag> tags = new HashSet<>();
		for(String tagName : tagNames) {
			tags.add(findByName(tagName));
		}
		return tags;
	}
}
