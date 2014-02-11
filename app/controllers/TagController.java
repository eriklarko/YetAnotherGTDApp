
package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.List;
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

	public static Result list() throws JsonProcessingException {
		return ok(Json.toJson(Tag.all()));
	}

	public static Result listNames() throws JsonProcessingException {
		List<String> tagNames = new ArrayList<>();
		for(Tag tag : Tag.all()) {
			tagNames.add(tag.name);
		}
		return ok(Json.toJson(tagNames));
	}

    @BodyParser.Of(BodyParser.Json.class)
    public static Result save() {
        JsonNode json = request().body().asJson();

        Form<Tag> f = new Form<>(Tag.class);
        Form<Tag> bound = f.bind(json);
        if (bound.hasErrors()) {
            return badRequest(bound.errorsAsJson());
        } else {
            Tag toCreate = bound.get();
            toCreate.save();

            ObjectNode result = Json.newObject();
            result.put("Tag", Json.toJson(toCreate));
            return ok(result);
        }
    }
}
