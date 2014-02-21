package search;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author Thinner
 */
public class JsonToSearchTree {

    public static Node parse(JsonNode json) {
        String type = json.get("type").asText().toLowerCase();
        switch (type) {
            case "and":
                return parseAnd(json);

            case "or":
                return parseOr(json);

            case "not":
                return parseNot(json);

            case "ideq":
                return parseIdEq(json);

            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    private static And parseAnd(JsonNode json) {
        Collection<Node> children = getChildren(json, "children");
        return new And(children);
    }

    private static Collection<Node> getChildren(JsonNode json, String childrenFieldName) {
        Collection<Node> children = new LinkedList<>();
        for(JsonNode child : json.get(childrenFieldName)) {
            children.add(parse(child));
        }
        return children;
    }

    private static Or parseOr(JsonNode json) {
        Collection<Node> children = getChildren(json, "children");
        return new Or(children);
    }

    private static Not parseNot(JsonNode json) {
        Node child = parse(json.get("child"));
        return new Not(child);
    }

    private static IdEq parseIdEq(JsonNode json) {
        Long tagId = Long.parseLong(json.get("id").asText());
        return new IdEq(tagId);
    }
}
