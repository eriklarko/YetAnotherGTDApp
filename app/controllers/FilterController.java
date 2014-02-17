package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Sets;
import models.Filter;
import models.Tag;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import static play.mvc.Controller.request;
import play.mvc.Result;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;
import services.FilterService;
import services.NoteService;
import services.TagNameService;
import services.TagsService;
import util.Validation;
import utils.JsonUtil;

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

		return ok(Json.toJson(NoteService.findNotesTaggedWith(filter.tags)));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result save() {
		JsonNode json = request().body().asJson();

		Validation<String> validation = validateSaveJson(json);
		if (validation.hasErrors()) {
			return badRequest(Json.toJson(validation.errors()));
		} else {
			Tag[] tags = JsonUtil.getTagsFromNames(json.findValue("tags"));
			String name = validation.getParsed("name").iterator().next();

			Filter newFilter = FilterService.createFilter(name, Sets.newHashSet(tags));
			return ok(Json.toJson(newFilter));
		}
	}

	public static Result delete(Long id) {
		Filter filter = Filter.find.byId(id);
		if (filter == null) {
			return badRequest("No filter with id " + id + " found.");
		}
		FilterService.remove(filter);
		return ok();
	}

	private static Validation<String> validateSaveJson(JsonNode json) {
		JsonNode nameJson = json.get("name");
		JsonNode tagsJson = json.get("tags");
		System.out.println("NAME: " + nameJson);

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

		if (nameJson == null || nameJson.textValue().isEmpty()) {
			validation.reject("name", "No name given");
		} else {
			validation.successfullyParsed("name", nameJson.asText());
		}

		return validation;
	}

	public static Result getNotesInFilter(Long id) {
		Filter filter = Filter.find.byId(id);
		if (filter == null) {
			return badRequest("Could not find filter with id " + id);
		}

		return ok(Json.toJson(NoteService.findNotesTaggedWith(filter.tags)));
	}

	public static Result addTag(Long filterId, String tagName) {
		Filter filter = Filter.find.byId(filterId);
		if (filter == null) {
			return badRequest("Could not find filter with id " + filterId);
		}

		Tag tag = TagNameService.findOrCreateTagFromName(tagName);

		FilterService.addTag(filter, tag);
		return ok(Json.toJson(filter));
	}

	public static Result removeTag(Long filterId, String tagName) {
		Filter filter = Filter.find.byId(filterId);
		if (filter == null) {
			return badRequest("Could not find filter with id " + filterId);
		}

		Tag tag = TagsService.findByName(tagName);
		if (tag == null) {
			return badRequest("Could not find tag with name " + tagName);
		}

		FilterService.removeTagFromFilter(filter, tag);
		return ok(Json.toJson(filter));
	}
}
