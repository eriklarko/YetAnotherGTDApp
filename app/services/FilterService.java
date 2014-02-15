package services;

import java.util.Collection;
import models.Filter;

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
}
