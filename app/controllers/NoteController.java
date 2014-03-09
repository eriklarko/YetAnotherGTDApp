package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Sets;
import java.util.Arrays;
import models.Note;
import models.Tag;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.NoteService;
import services.TagNameService;
import services.TagService;
import utils.Validation;
import utils.JsonUtil;

/**
 *
 * @author Thinner
 */
public class NoteController extends Controller {

	/**
	 * Lists all notes
	 *
	 * @throws JsonProcessingException
	 */
	public static Result list() throws JsonProcessingException {
		return ok(Json.toJson(NoteService.instance().findAllCurrentUsersNotes()));
	}

	/**
	 * Creates or updates a note
	 *
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result save() {
		JsonNode json = request().body().asJson();
		if (json == null) {
			return badRequest("Expected json");
		}

		Validation<String> validation = validateSaveJson(json);
		if (validation.hasErrors()) {
			return badRequest(Json.toJson(validation.errors()));
		} else {
			Tag[] tags = JsonUtil.getTagsFromNames(json.get("tags"));
			String payload = validation.getParsed("payload").iterator().next();

			Note newNote = NoteService.instance().create(payload, Sets.newHashSet(tags));
			return ok(Json.toJson(newNote));
		}
	}

	private static Validation<String> validateSaveJson(JsonNode json) {
		JsonNode payload = json.get("payload");
		JsonNode tagsJson = json.get("tags");

		Tag[] tags;
		Validation<String> validation = new Validation<>();
		if (tagsJson == null || !tagsJson.isArray()) {
			validation.reject("tags", "No tags given");
		} else {
			tags = JsonUtil.getTagsFromNames(tagsJson);
			if (tags == null || tags.length == 0 || TagService.instance().hasOnlyEmptyNames(Sets.newHashSet(tags))) {
				validation.reject("tags", "No tags given");
			} else {
				validation.successfullyParsed("tags", tagsJson.asText());
			}
		}

		if (payload == null || payload.textValue().isEmpty()) {
			validation.reject("payload", "No payload given");
		} else {
			validation.successfullyParsed("payload", payload.asText());
		}

		return validation;
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result update(Long id) {
		JsonNode json = request().body().asJson();

		Note note = NoteService.instance().byId(id);
		if (note == null) {
			return badRequest("Unable to find note with id " + id);
		}

		if (json.get("payload") != null) {
			String payload = json.get("payload").asText();
			if (payload.isEmpty()) {
				return badRequest(Json.toJson("No payload specified"));
			}
			NoteService.instance().updatePayload(note, payload);
		}

		if (json.get("tags") != null) {
			if (!json.get("tags").isArray()) {
				return badRequest(Json.toJson("Tags needs to be an array"));
			}

			Tag[] tags = JsonUtil.getTagsFromNames(json.get("tags"));
			if (tags == null || TagService.instance().hasOnlyEmptyNames(Sets.newHashSet(tags))) {
				return badRequest(Json.toJson("You must specify non-empty tag names"));
			}

			NoteService.instance().replaceTags(note, Arrays.asList(tags));
		}

		return ok(Json.toJson(note));
	}

	public static Result remove(Long id) {
		Note note = NoteService.instance().byId(id);
		if (note == null) {
			return badRequest("Could not find note with id " + id);
		} else {
			NoteService.instance().remove(note);
			return ok();
		}
	}

	public static Result tag(Long noteId, String tagName) {
		Note note = NoteService.instance().byId(noteId);
		if (note == null) {
			return badRequest("Could not find note with id " + noteId);
		}

		Tag tag = TagNameService.findOrCreateTagFromName(tagName);

		NoteService.instance().addTag(note, tag);
		return ok(Json.toJson(note));
	}

	public static Result untag(Long noteId, Long tagId) {
		Note note = NoteService.instance().byId(noteId);
		if (note == null) {
			return badRequest("Could not find note with id " + noteId);
		}

		Tag tag = TagService.instance().byId(tagId);
		if (tag == null) {
			return badRequest("Could not find tag with id " + tagId);
		}

		NoteService.instance().removeTag(note, tag);
		return ok(Json.toJson(note));
	}
}
