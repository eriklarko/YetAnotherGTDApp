package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Arrays;
import models.Filter;
import models.TaggableObject;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import static play.mvc.Controller.request;
import play.mvc.Result;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;
import services.FilterService;
import services.TaggableService;
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
}
