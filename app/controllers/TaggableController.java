package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import models.TaggableObject;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.TaggableService;
import util.Validation;

/**
 *
 * @author Thinner
 */
public class TaggableController extends Controller {

	/**
	 * Lists all taggables
	 * @throws JsonProcessingException
	 */
	public static Result list() throws JsonProcessingException {
		return ok(Json.toJson(TaggableObject.all()));
	}

	/**
	 * Creates or updates a taggable
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result save() {
		JsonNode json = request().body().asJson();
		return save(json);
	}

	private static Result save(JsonNode json) {
		Validation<String> validation = validateSaveJson(json);
		if (validation.hasErrors()) {
			return badRequest(Json.toJson(validation.errors()));
		} else {
			String[] tagNames = getTagNames(json.findValue("tags"));
			String payload = validation.getParsed("payload").iterator().next();

			TaggableObject newTaggable = TaggableService.createTaggable(payload, Arrays.asList(tagNames));
			return ok(Json.toJson(newTaggable));
		}
	}

	private static Validation<String> validateSaveJson(JsonNode json) {
		JsonNode payload = json.findValue("payload");
		JsonNode tags = json.findValue("tags");

		String[] tagNames;
		Validation<String> validation = new Validation<>();
		if (tags == null || !tags.isArray()) {
			validation.reject("tags", "No tags given");
		} else {
			tagNames = getTagNames(tags);
			if (tagNames == null || tagNames.length == 0 || onlyEmptyStrings(tagNames)) {
				validation.reject("tags", "No tags given");
			} else {
				validation.successfullyParsed("tags", tags.asText());
			}
		}

		if (payload == null || payload.textValue().isEmpty()) {
			validation.reject("payload", "No payload given");
		} else {
			validation.successfullyParsed("payload", payload.asText());
		}

		return validation;
	}

	private static String[] getTagNames(JsonNode tags) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(tags.traverse(), String[].class);
		} catch (IOException ex) {
			return null;
		}
	}

	private static boolean onlyEmptyStrings(String[] strings) {
		for (String string : strings) {
			if (! (string == null || string.trim().isEmpty())) {
				return false;
			}
		}
		return true;
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result remove() {
		JsonNode json = request().body().asJson();
		JsonNode idNode = json.findValue("id");

		long id;
		try {
			id = Long.parseLong(idNode.asText());
		} catch (NumberFormatException ex) {
			return badRequest("Empty or non-integral id");
		}

		TaggableObject obj = TaggableObject.find.byId(id);
		if (obj == null) {
			return badRequest("Could not find taggable object with id " + id);
		} else {
			TaggableService.remove(obj);
			return ok();
		}
	}
}
