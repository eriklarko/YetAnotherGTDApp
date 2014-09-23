package services;

import models.User;
import play.db.ebean.Model;

/**
 *
 * @author eriklark
 */
public class UserService {

	private static final Model.Finder<Long, User> find = new Model.Finder(Long.class, User.class);

	private static User currentUser;

	public static User getCurrentUser() {
		if (currentUser == null) {
			currentUser = find.findUnique();
			if (currentUser == null) {
				currentUser = new User();
				currentUser.email = "hej@hej.hej";
				currentUser.password = "lol";
				currentUser.save();
			}
		}

		return currentUser;
	}
}
