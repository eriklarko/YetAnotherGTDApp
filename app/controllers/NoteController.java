package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import models.Tag;
import models.Note;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.NoteService;
import services.TagNameService;
import services.TagsService;
import util.Validation;
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
		return ok(Json.toJson(Note.all()));
	}

	/**
	 * Creates or updates a note
	 *
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result save() {
		JsonNode json = request().body().asJson();

		Validation<String> validation = validateSaveJson(json);
		if (validation.hasErrors()) {
			return badRequest(Json.toJson(validation.errors()));
		} else {
			Tag[] tags = JsonUtil.getTagsFromNames(json.get("tags"));
			String payload = validation.getParsed("payload").iterator().next();

			Note newNote = NoteService.create(payload, Sets.newHashSet(tags));
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
			if (tags == null || tags.length == 0 || TagsService.hasOnlyEmptyNames(Sets.newHashSet(tags))) {
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
	public static Result remove(Long id) {
		Note note = Note.find.byId(id);
		if (note == null) {
			return badRequest("Could not find note with id " + id);
		} else {
			NoteService.remove(note);
			return ok();
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result tag(Long noteId, String tagName) {
		Note note = Note.find.byId(noteId);
		if (note == null) {
			return badRequest("Could not find note with id " + noteId);
		}

		Tag tag = TagNameService.findOrCreateTagFromName(tagName);

		NoteService.addTag(note, tag);
		return ok(Json.toJson(note));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result untag(Long noteId, String tagName) {
		Note note = Note.find.byId(noteId);
		if (note == null) {
			return badRequest("Could not find note with id " + noteId);
		}

		Tag tag = TagsService.findByName(tagName);
		if (tag == null) {
			return badRequest("Could not find tag with name " + tagName);
		}
		
		NoteService.removeTag(note, tag);
		return ok(Json.toJson(note));
	}
}
