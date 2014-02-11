package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import models.Tag;
import models.TaggableObject;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import util.Validation;

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
		System.out.println(json);

		Validation<String> validation = validateSaveJson(json);
		if (validation.hasErrors()) {
			return badRequest(Json.toJson(validation.errors()));
		} else {
			String[] tagNames = getTagNames(json.findValue("tags"));

			TaggableObject toCreate = new TaggableObject();
			toCreate.payload = validation.getParsed("payload").iterator().next();
			toCreate.tags = findOrCreateTags(tagNames);
			toCreate.save();

			return ok(Json.toJson(toCreate));
		}
	}

	private static Validation<String> validateSaveJson(JsonNode json) {
		JsonNode payload = json.findValue("payload");
		JsonNode tags = json.findValue("tags");

		String[] tagNames = null;
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

	private static List<Tag> findOrCreateTags(String[] tagNames) {
		List<Tag> tags = new ArrayList<>(tagNames.length);
		for (String tagName : tagNames) {
			tags.add(findOrCreateTag(tagName));
		}
		return tags;
	}

	private static Tag findOrCreateTag(String tagName) {
		Tag tag = Tag.find.where().eq("name", tagName).findUnique();
		if (tag == null) {
			return createNewTag(tagName);
		} else {
			return tag;
		}
	}

	private static Tag createNewTag(String tagName) {
		Tag tag = new Tag();
		tag.name = tagName;
		tag.save();
		return tag;
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
			remove(obj);
			return ok();
		}
	}

	private static void remove(TaggableObject obj) {
		List<Tag> tags = obj.tags;
		for (Tag tag : tags) {
			List<TaggableObject> objectsWithTag = TaggableObject.find.where().eq("tags.name", tag.name).findList();
			if (objectsWithTag.isEmpty()) {
				tag.delete();
			}
		}
		obj.deleteManyToManyAssociations("tags");
		obj.delete();
	}
}
