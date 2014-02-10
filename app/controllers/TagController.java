
package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Tag;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import static play.mvc.Controller.request;
import play.mvc.Result;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;

/**
 *
 * @author Thinner
 */
public class TagController extends Controller {

    @BodyParser.Of(BodyParser.Json.class)
    public static Result save() {
        JsonNode json = request().body().asJson();

        Form<Tag> f = new Form<>(Tag.class);
        Form<Tag> bound = f.bind(json);
        if (bound.hasErrors()) {
            return badRequest(bound.errorsAsJson());
        } else {
            Tag toCreate = bound.get();
            Tag.create(toCreate);

            ObjectNode result = Json.newObject();
            result.put("Tag", Json.toJson(toCreate));
            return ok(result);
        }
    }
}
