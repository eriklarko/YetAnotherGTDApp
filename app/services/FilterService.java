package services;

import java.util.NoSuchElementException;
import java.util.Set;
import models.Filter;
import models.Tag;
import play.db.ebean.Transactional;

/**
 *
 * @author eriklark
 */
public class FilterService {

	@Transactional
	public static Filter createFilter(String name, Set<Tag> tags) {
		Filter filter = new Filter();
		filter.name = name;
		filter.tags = TagsService.findOrCreateTags(tags);
		filter.save();

		return filter;
	}

	@Transactional
	public static void remove(Filter filter) {
		filter.delete();
	}

    @Transactional
	public static void removeTagFromFilter(Filter filter, Tag tagToRemove) throws NoSuchElementException {
		boolean tagRemoved = filter.tags.remove(tagToRemove);

		if (tagRemoved) {
			filter.save();
		} else {
			throw new NoSuchElementException("Unable to find tag " + tagToRemove.name + " in filter " + filter.id);
		}
	}

	@Transactional
	public static void addTag(Filter filter, Tag tag) {
		filter.tags.add(tag);
		filter.save();
	}
}
