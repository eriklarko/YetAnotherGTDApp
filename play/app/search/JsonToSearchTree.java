package search;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import models.Tag;
import services.TagService;

/**
 *
 * @author Thinner
 */
public class JsonToSearchTree {

    public static Node parse(JsonNode json) {
        return parse(null, json);
    }

    private static Node parse(Node parent, JsonNode json) {
        String type = json.get("type").asText().toLowerCase();
        switch (type) {
            case "and":
                return parseAnd(parent, json);

            case "or":
                return parseOr(parent, json);

            case "not":
                return parseNot(parent, json);

            case "ideq":
                return parseIdEq(parent, json);

            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    private static And parseAnd(Node parent, JsonNode json) {
        Set<Node> children = new HashSet<>();
        And and = new And(parent, children);

        Collection<Node> parsedChildren = getChildren(and, json, "children");
        children.addAll(parsedChildren);

        return and;
    }

    private static Collection<Node> getChildren(Node parent, JsonNode json, String childrenFieldName) {
        Collection<Node> children = new LinkedList<>();
        for(JsonNode child : json.get(childrenFieldName)) {
            children.add(parse(parent, child));
        }
        return children;
    }

    private static Or parseOr(Node parent, JsonNode json) {
        Set<Node> children = new HashSet<>();
        Or or = new Or(parent, children);

        Collection<Node> parsedChildren = getChildren(or, json, "children");
        children.addAll(parsedChildren);

        return or;
    }

    private static Not parseNot(Node parent, JsonNode json) {
        Not not = new Not(parent);
        Node child = parse(not, json.get("child"));
        not.setChild(child);

        return not;
    }

    private static IdEq parseIdEq(Node parent, JsonNode json) {
        Long tagId = Long.parseLong(json.get("id").asText());
        Tag tag = TagService.instance().byId(tagId);
        if (tag == null) {
            throw new IllegalArgumentException("No tag with id " + tagId + " found");
        }
        return new IdEq(parent, tag);
    }
}
