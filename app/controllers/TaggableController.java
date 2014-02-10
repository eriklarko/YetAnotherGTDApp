package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.TaggableObject;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

/**
 *
 * @author Thinner
 */
public class TaggableController extends Controller {

    public static Result list() throws JsonProcessingException {
        return ok(Json.toJson(TaggableObject.all()));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result save() {
        JsonNode json = request().body().asJson();
        Form<TaggableObject> f = new Form<>(TaggableObject.class);
        Form<TaggableObject> bound = f.bind(json);
        if (bound.hasErrors()) {
            return badRequest(bound.errorsAsJson());
        } else {
            TaggableObject toCreate = bound.get();
            TaggableObject.create(toCreate);

            ObjectNode result = Json.newObject();
            result.put("taggable", Json.toJson(toCreate));
            return ok(result);
        }
    }
}
