package models;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import play.db.ebean.Model;

/**
 *
 * @author Thinner
 */
@Entity
public class Note extends Model {

    @Id
    public Long id;

    @ManyToMany
    public Set<Tag> tags = new HashSet<>();

    @Column
	@NotNull
    public String payload;

    public static Finder<Long, Note> find = new Finder(Long.class, Note.class);

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 41 * hash + Objects.hashCode(this.id);
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
		final Note other = (Note) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		return true;
	}
}
