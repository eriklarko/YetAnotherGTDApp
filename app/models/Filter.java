package models;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import play.db.ebean.Model;

/**
 *
 * @author eriklark
 */

@Entity
public class Filter extends Model {

	@Id
	public Long id;

	@Column
	public String name;

	@ManyToMany
	public Set<Tag> tags = new HashSet<>();

	public static Finder<Long, Filter> find = new Finder(Long.class, Filter.class);
}
