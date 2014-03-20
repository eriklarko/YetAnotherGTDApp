package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Set;
import models.Tag;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import static play.mvc.Controller.request;
import play.mvc.Result;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;
import services.NoteService;
import services.TagService;

/**
 *
 * @author Thinner
 */
public class TagController extends Controller {

	public static Result list() throws JsonProcessingException {
		return ok(Json.toJson(TagService.instance().getAllCurrentUsersTags()));
	}

	public static Result listWithNumberOfNotes() {
		ArrayNode toReturn = new ArrayNode(JsonNodeFactory.instance);
		Set<Tag> allTags = TagService.instance().getAllCurrentUsersTags();
		for (Tag tag : allTags) {
			ObjectNode tagJson = (ObjectNode) Json.toJson(tag);
			tagJson.put("numNotes", Json.toJson(NoteService.instance().findNotesWithTag(tag)).size());

			toReturn.add(tagJson);
		}

		return ok(toReturn);
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
            return ok(Json.toJson(toCreate));
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
	public static Result updateName(Long id) {
		JsonNode json = request().body().asJson();

		String newName;
		try {
			newName = json.get("name").asText();
		} catch (NullPointerException ex) {
			return badRequest(Json.toJson("No name specified"));
		}

		if (newName.isEmpty()) {
			return badRequest(Json.toJson("No name specified"));
		}

		Tag tag = TagService.instance().byId(id);
		if (tag == null) {
			return badRequest("Unable to find tag with id " + id);
		}

		TagService.instance().updateName(tag, newName);
		return ok(Json.toJson(tag));
	}
}
