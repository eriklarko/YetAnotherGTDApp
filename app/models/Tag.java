package models;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
    public String name;

    public static Finder<Long, Tag> find = new Finder(Long.class, Tag.class);

    public static Collection<Tag> all() {
        return find.all();
    }
}
