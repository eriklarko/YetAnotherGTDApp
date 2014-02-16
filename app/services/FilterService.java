package services;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import models.Filter;
import models.Tag;
import play.db.ebean.Transactional;

/**
 *
 * @author eriklark
 */
public class FilterService {

	public static Filter createFilter(String name, Collection<String> tagNames) {
		Filter filter = new Filter();
		filter.name = name;
		filter.tags = TagsService.findOrCreateTags(tagNames);
		filter.save();

		return filter;
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
	public static void addTags(Filter filter, Collection<String> tagNames) {
		List<Tag> newTags = TagsService.findOrCreateTags(tagNames);
		filter.tags.addAll(newTags);
		filter.save();
	}
}
