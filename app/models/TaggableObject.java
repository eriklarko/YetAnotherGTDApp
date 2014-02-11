package models;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import play.db.ebean.Model;

/**
 *
 * @author Thinner
 */
@Entity
public class TaggableObject extends Model {

    @Id
    public Long id;

    @ManyToMany
    public List<Tag> tags = new LinkedList<>();

    @Column
    public String payload;

    public static Finder<Long, TaggableObject> find = new Finder(Long.class, TaggableObject.class);

    public static Collection<TaggableObject> all() {
        return find.all();
    }
}
