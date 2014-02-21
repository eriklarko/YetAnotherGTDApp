package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import models.Filter;
import models.Tag;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import static play.mvc.Controller.request;
import play.mvc.Result;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;
import search.JsonToSearchTree;
import search.Node;
import search.SearchTreeToJson;
import services.FilterService;
import services.TagNameService;

/**
 *
 * @author eriklark
 */
public class FilterController extends Controller {

    public static Result list() {
        return ok(Json.toJson(Filter.find.all()));
    }

    public static Result listNotes(Long id) {
        Filter filter = Filter.find.byId(id);
        if (filter == null) {
            return badRequest("No filter with id " + id + " found.");
        }

        return ok(Json.toJson(filter.getSearchTree().execute()));
    }

    public static Result getSearchTree(Long id) {
        Filter filter = Filter.find.byId(id);
        if (filter == null) {
            return badRequest("No filter with id " + id + " found.");
        }

        return ok(SearchTreeToJson.parse(filter.getSearchTree()));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result save() {
        JsonNode json = request().body().asJson();

        String name = json.get("name").asText();
        findNewTagsAndCreateThem(json);
        Node searchTree = JsonToSearchTree.parse(json.get("searchTree"));

        Filter newFilter = FilterService.createFilter(name, searchTree);
        return ok(Json.toJson(newFilter));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result update(Long id) {
        JsonNode json = request().body().asJson();

        String name = json.get("name").asText();
        findNewTagsAndCreateThem(json);
        Node searchTree = JsonToSearchTree.parse(json.get("searchTree"));

        Filter f = Filter.find.byId(id);
        FilterService.updateFilter(f, name, searchTree);

        ObjectNode node = (ObjectNode) Json.toJson(f);
        node.put("searchTree", SearchTreeToJson.parse(f.getSearchTree()));

        return ok(node);
    }

    private static void findNewTagsAndCreateThem(JsonNode json) {
        List<JsonNode> ids = json.findParents("name");
        for(JsonNode parent : ids) {

            JsonNode type = parent.get("type");
            JsonNode id = parent.get("id");
            JsonNode nameNode = parent.get("name");

            if (type != null && id == null) {
                Tag t = TagNameService.findOrCreateTagFromName(nameNode.asText());
                ObjectNode on = (ObjectNode) parent;
                on.put("id", t.id);
            }
        }
    }

    public static Result delete(Long id) {
        Filter filter = Filter.find.byId(id);
        if (filter == null) {
            return badRequest("No filter with id " + id + " found.");
        }
        FilterService.remove(filter);
        return ok();
    }
}
