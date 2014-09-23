package services;

import java.util.Collection;
import models.Filter;
import play.db.ebean.Transactional;
import search.Node;

/**
 *
 * @author eriklark
 */
public class FilterService extends BaseService<Filter> {

	private static final FilterService instance = new FilterService();

	public static FilterService instance() {
		return instance;
	}

	private FilterService() {
		super(Filter.class);
	}

	public Collection<Filter> getAllCurrentUsersFilters() {
		return find().findList();
	}

	@Transactional
	public Filter createFilter(String name, Node searchTree) {
		Filter filter = new Filter();
		filter.owner = UserService.getCurrentUser();
		filter.name = name;
		filter.setSearchTree(searchTree);
		filter.save();

		return filter;
	}

	@Transactional
	public void remove(Filter filter) {
		filter.delete();
	}

    public void updateFilter(Filter filter, String name, Node searchTree) {
        filter.name = name;
        filter.setSearchTree(searchTree);
        filter.save();
    }

	public void star(Filter filter) {
		filter.starred = true;
		filter.save();
	}

	public void unstar(Filter filter) {
		filter.starred = false;
		filter.save();
	}
}
