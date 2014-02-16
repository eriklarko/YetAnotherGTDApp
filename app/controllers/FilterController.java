package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Arrays;
import java.util.NoSuchElementException;
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
import services.TaggableService;
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

	@BodyParser.Of(BodyParser.Json.class)
	public static Result save() {
		JsonNode json = request().body().asJson();

		Validation<String> validation = validateSaveJson(json);
		if (validation.hasErrors()) {
			return badRequest(Json.toJson(validation.errors()));
		} else {
			String[] tagNames = JsonUtil.getTagNames(json.findValue("tags"));
			String name = validation.getParsed("name").iterator().next();

			Filter newFilter = FilterService.createFilter(name, Arrays.asList(tagNames));
			return ok(Json.toJson(newFilter));
		}
	}

	private static Validation<String> validateSaveJson(JsonNode json) {
		JsonNode name = json.findValue("name");
		JsonNode tags = json.findValue("tags");

		String[] tagNames;
		Validation<String> validation = new Validation<>();
		if (tags == null || !tags.isArray()) {
			validation.reject("tags", "No tags given");
		} else {
			tagNames = JsonUtil.getTagNames(tags);
			if (tagNames == null || tagNames.length == 0 || JsonUtil.onlyEmptyStrings(tagNames)) {
				validation.reject("tags", "No tags given");
			} else {
				validation.successfullyParsed("tags", tags.asText());
			}
		}

		if (name == null || name.textValue().isEmpty()) {
			validation.reject("name", "No name given");
		} else {
			validation.successfullyParsed("name", name.asText());
		}

		return validation;
	}

    public static Result getTaggablesInFilter() {
        JsonNode json = request().body().asJson();
		JsonNode filterIdJson = json.findValue("filterId");

		long filterId;
		try {
			filterId = Long.parseLong(filterIdJson.asText());
		} catch (NullPointerException | NumberFormatException ex) {
			return badRequest("Empty or non-integral ids");
		}

		Filter filter = Filter.find.byId(filterId);
		if (filter == null) {
			return badRequest("Could not find filter with id " + filterId);
		}

        return ok(Json.toJson(TaggableService.findTaggablesTaggedWith(filter.tags)));
    }

    public static Result addTags() {
        JsonNode json = request().body().asJson();
		JsonNode filterIdJson = json.findValue("filterId");
		JsonNode tagNamesJson = json.findValue("tagNames");

		long filterId;
		String[] tagNames;
		try {
			filterId = Long.parseLong(filterIdJson.asText());
			tagNames = JsonUtil.getTagNames(tagNamesJson);
		} catch (NullPointerException | NumberFormatException ex) {
			return badRequest("Empty or non-integral ids");
		}

		Filter filter = Filter.find.byId(filterId);
		if (filter == null) {
			return badRequest("Could not find taggable object with id " + filterId);
		} else if (tagNames == null || JsonUtil.onlyEmptyStrings(tagNames)) {
			return badRequest("No tags found");
		}

		FilterService.addTags(filter, Arrays.asList(tagNames));
		return ok(Json.toJson(filter));
    }

    public static Result removeTag() {
        JsonNode json = request().body().asJson();
		JsonNode filterIdJson = json.findValue("filterId");
		JsonNode tagNameJson = json.findValue("tagName");

		long filterId;
		String tagName;
		try {
			filterId = Long.parseLong(filterIdJson.asText());
			tagName = tagNameJson.asText();
		} catch (NullPointerException | NumberFormatException ex) {
			return badRequest("Empty or non-integral ids");
		}

		Filter filter = Filter.find.byId(filterId);
		if (filter == null) {
			return badRequest("Could not find filter with id " + filterId);
		}

		Tag tag;
		try {
			tag = TagsService.findByName(tagName);
		} catch (NoSuchElementException ex) {
			return badRequest("Could not find tag with name " + tagName);
		}

		FilterService.removeTagFromFilter(filter, tag);
		return ok(Json.toJson(filter));
    }
}
