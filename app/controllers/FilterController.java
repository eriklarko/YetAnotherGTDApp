package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Filter;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import static play.mvc.Controller.request;
import play.mvc.Result;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;
import search.JsonToSearchTree;
import search.Node;
import services.FilterService;

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

    @BodyParser.Of(BodyParser.Json.class)
    public static Result save() {
        JsonNode json = request().body().asJson();

        String name = json.get("name").asText();
        Node searchTree = JsonToSearchTree.parse(json.get("searchTree"));

        Filter newFilter = FilterService.createFilter(name, searchTree);
        return ok(Json.toJson(newFilter));
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
