package services;

import models.Filter;
import play.db.ebean.Transactional;
import search.Node;

/**
 *
 * @author eriklark
 */
public class FilterService {

	@Transactional
	public static Filter createFilter(String name, Node searchTree) {
		Filter filter = new Filter();
		filter.name = name;
		filter.setSearchTree(searchTree);
		filter.save();

		return filter;
	}

	@Transactional
	public static void remove(Filter filter) {
		filter.delete();
	}

    public static void updateFilter(Filter filter, String name, Node searchTree) {
        filter.name = name;
        filter.setSearchTree(searchTree);
        filter.save();
    }
}
