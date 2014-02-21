package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;
import play.db.ebean.Model;
import search.JsonToSearchTree;
import search.Node;
import search.SearchTreeToJson;

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

	@Lob
	String searchTree;

    @Transient
    private Node searchTreeCache;

	public static Finder<Long, Filter> find = new Finder(Long.class, Filter.class);

    public Node getSearchTree() {
        if (this.searchTreeCache == null) {
            try {
                JsonNode jsonNode = new ObjectMapper().readTree(searchTree);
                searchTreeCache = JsonToSearchTree.parse(jsonNode);
            } catch (IOException ex) {
                throw new RuntimeException("Unable to parse search tree", ex);
            }
        }
        return this.searchTreeCache;
    }

    public void setSearchTree(Node searchTree) {
        this.searchTree = SearchTreeToJson.parse(searchTree).toString();
        this.searchTreeCache = null;
    }


}
