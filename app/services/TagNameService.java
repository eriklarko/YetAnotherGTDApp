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
			return TagService.instance().findByName(tagName);
		} catch (NoSuchElementException ex) {
			return TagService.instance().createNewTagFromName(tagName);
		}
	}

	public static Set<Tag> findTagsByName(Iterable<String> tagNames) throws NoSuchElementException {
		Set<Tag> tags = new HashSet<>();
		for(String tagName : tagNames) {
			tags.add(TagService.instance().findByName(tagName));
		}
		return tags;
	}
}
