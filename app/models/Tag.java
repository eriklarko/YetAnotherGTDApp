package models;

import java.util.Collection;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import play.db.ebean.Model;

/**
 *
 * @author Thinner
 */
@Entity
public class Tag extends Model {

    @Id
    public Long id;

    @Column
	@NotEmpty
	@NotNull
	public String name;

    public static Finder<Long, Tag> find = new Finder(Long.class, Tag.class);

    public static Collection<Tag> all() {
        return find.all();
    }

	@Override
	public int hashCode() {
		int hash = 7;
		if(this.id == null) {
			hash = 23 * hash + Objects.hashCode(this.name);
		} else {
			hash = 23 * hash + Objects.hashCode(this.id);
		}
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Tag other = (Tag) obj;


		if(this.id == null) {
			return Objects.equals(this.name, other.name);
		} else {
			return Objects.equals(this.id, other.id);
		}
	}

	@Override
	public String toString() {
		return "Tag{" + "id=" + id + ", name=" + name + '}';
	}
}
