package services;

import com.avaje.ebean.ExpressionList;
import play.db.ebean.Model;

/**
 *
 * @author eriklark
 */
public class BaseService<T> {

	private final Model.Finder<Long, T> find;

	public BaseService(Class<T> clazz) {
		find = new Model.Finder(Long.class, clazz);
	}

	ExpressionList<T> find() {
		return find.where().eq("owner", UserService.getCurrentUser());
	}

	public T byId(Long id) {
		return find().eq("id", id).findUnique();
	}

}
