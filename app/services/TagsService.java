package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import models.Tag;
import play.db.ebean.Transactional;

/**
 *
 * @author eriklark
 */
public class TagsService {

	public static List<String> getTagNames(Collection<Tag> tags) {
		List<String> tagNames = new ArrayList<>();
		for(Tag tag : tags) {
			tagNames.add(tag.name);
		}

		return tagNames;
	}

	@Transactional
	public static List<Tag> findOrCreateTags(Collection<String> tagNames) {
		List<Tag> tags = new ArrayList<>(tagNames.size());
		for (String tagName : tagNames) {
			tags.add(findOrCreateTag(tagName));
		}
		return tags;
	}

	private static Tag findOrCreateTag(String tagName) {
		Tag tag = Tag.find.where().eq("name", tagName).findUnique();
		if (tag == null) {
			return createNewTag(tagName);
		} else {
			return tag;
		}
	}

	private static Tag createNewTag(String tagName) {
		Tag tag = new Tag();
		tag.name = tagName;
		tag.save();
		return tag;
	}
}
