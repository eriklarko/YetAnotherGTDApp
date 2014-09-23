package services;

import java.util.HashSet;
import removewhenjava8.Optional;
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
		Optional<Tag> tag = TagService.instance().findByName(tagName);
		if (tag.isPresent()) {
			return tag.get();
		} else {
			return TagService.instance().createNewTagFromName(tagName);
		}
	}

	public static Set<Tag> findTagsByName(Iterable<String> tagNames) {
		Set<Tag> tags = new HashSet<>();
		for(String tagName : tagNames) {
			Optional<Tag> tag = TagService.instance().findByName(tagName);
			if (tag.isPresent()) {
				tags.add(tag.get());
			}
		}
		return tags;
	}
}
