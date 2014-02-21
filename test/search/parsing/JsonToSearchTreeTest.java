
package search.parsing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;
import search.And;
import search.IdEq;
import search.JsonToSearchTree;
import search.Node;
import search.Not;

/**
 *
 * @author Thinner
 */
public class JsonToSearchTreeTest {

    private static final String ID_EQ = "{\"type\": \"ideq\", \"id\": 1}";

    @Test
    public void idEq() {
        JsonNode jn = toJson(ID_EQ);

        Node actual = JsonToSearchTree.parse(jn);
        Assert.assertTrue("Type is wrong", actual instanceof IdEq);
        Assert.assertEquals((Long) 1L, ((IdEq) actual).getId());
    }

    @Test
    public void not() {
        JsonNode jn = toJson("{\"type\": \"not\", \"child\": " + ID_EQ + "}");

        Node actual = JsonToSearchTree.parse(jn);
        Assert.assertTrue("Type is wrong", actual instanceof Not);
        Assert.assertTrue("Child type is wrong", ((Not) actual).getChild() instanceof IdEq);
    }

    @Test
    public void and() {
        JsonNode jn = toJson("{\"type\": \"and\", \"children\": [" + ID_EQ + "," + ID_EQ + "]}");

        Node actual = JsonToSearchTree.parse(jn);
        Assert.assertTrue("Type is wrong", actual instanceof And);

        for (Node child : ((And) actual).getChildren()) {
            Assert.assertTrue("Child type is wrong", child instanceof IdEq);
        }
    }

    private JsonNode toJson(String json) {
        try {
            return new ObjectMapper().readTree(json);
        } catch (IOException ex) {
            ex.printStackTrace();
            Assert.fail("Unable to parse JSON");
            throw new Error();
        }
    }
}
