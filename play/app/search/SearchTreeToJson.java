package search;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import play.libs.Json;

/**
 *
 * @author Thinner
 */
public class SearchTreeToJson {

    public static JsonNode parse(Node searchTree) {
        return Json.toJson(doParse(searchTree));
    }

    private static Map<String, Object> doParse(Node searchTree) {

        if (searchTree instanceof And) {
            return parseAnd((And) searchTree);
        }
        if (searchTree instanceof Or) {
            return parseOr((Or) searchTree);
        }
        if (searchTree instanceof Not) {
            return parseNot((Not) searchTree);
        }
        if (searchTree instanceof IdEq) {
            return parseIdEq((IdEq) searchTree);
        }

        throw new IllegalArgumentException("Unknown node type");
    }

    private static Map<String, Object> parseAnd(And and) {
        Map<String, Object> toReturn = new HashMap<>();
        toReturn.put("type", "and");
        toReturn.put("children", parseChildren(and.getChildren()));

        return toReturn;
    }

    private static Collection<Map<String, Object>> parseChildren(Collection<Node> children) {
        Collection<Map<String, Object>> parsedChildren = new LinkedList<>();
        for (Node child : children) {
            parsedChildren.add(doParse(child));
        }
        return parsedChildren;
    }

    private static Map<String, Object> parseOr(Or or) {
        Map<String, Object> toReturn = new HashMap<>();
        toReturn.put("type", "or");
        toReturn.put("children", parseChildren(or.getChildren()));

        return toReturn;
    }

    private static Map<String, Object> parseNot(Not not) {
        Map<String, Object> toReturn = new HashMap<>();
        toReturn.put("type", "not");
        toReturn.put("child", doParse(not.getChild()));

        return toReturn;
    }

    private static Map<String, Object> parseIdEq(IdEq ideq) {
        Map<String, Object> toReturn = new HashMap<>();
        toReturn.put("type", "ideq");
        toReturn.put("id", ideq.getTag().id);
        toReturn.put("name", ideq.getTag().getName());

        return toReturn;
    }
}
