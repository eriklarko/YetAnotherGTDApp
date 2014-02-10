package models;

import java.util.Collection;
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
    public List<Tag> tags;

    @Column
    public String payload;

    public static Finder<Long, TaggableObject> find = new Finder(Long.class, TaggableObject.class);

    public static Collection<TaggableObject> all() {
        return find.all();
    }

    public static void create(TaggableObject taggable) {
        taggable.save();
    }

    public static void delete(Long id) {
        find.byId(id).delete();
    }
}
