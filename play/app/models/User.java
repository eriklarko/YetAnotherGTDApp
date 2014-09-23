package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import play.db.ebean.Model;

/**
 *
 * @author eriklark
 */
@Entity
public class User extends Model {

	@Id
	public Long id;

	@Column
	public String email;

	@Column
	public String password;

	public static Finder<Long, Tag> find = new Finder(Long.class, User.class);
}
