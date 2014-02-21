package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import search.JsonToSearchTree;
import search.Node;
import search.SearchTreeToJson;

/**
 *
 * @author Thinner
 */
public class SearchController extends Controller {

    @BodyParser.Of(BodyParser.Json.class)
    public static Result asd() {
        JsonNode json = request().body().asJson();
        Node searchTree = JsonToSearchTree.parse(json);
        return ok(SearchTreeToJson.parse(searchTree));
    }
}
