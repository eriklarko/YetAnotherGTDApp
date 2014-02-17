package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.dto.FilterDTO;
import controllers.dto.ManyTagNamesAndFilterDTO;
import controllers.dto.TagNameAndFilterDTO;
import java.util.Arrays;
import java.util.NoSuchElementException;
import models.Filter;
import models.Tag;
import play.data.Form;
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

        Form<FilterDTO> f = JsonUtil.getFromJson(FilterDTO.class, json);
		if (f.hasErrors()) {
			return badRequest(f.errorsAsJson());
		}

        FilterDTO dto = f.get();
		Filter filter = Filter.find.byId(dto.filterId);
		if (filter == null) {
			return badRequest("Could not find filter with id " + dto.filterId);
		}

        return ok(Json.toJson(TaggableService.findTaggablesTaggedWith(filter.tags)));
    }

    public static Result addTags() {
        JsonNode json = request().body().asJson();
		Form<ManyTagNamesAndFilterDTO> f = JsonUtil.getFromJson(ManyTagNamesAndFilterDTO.class, json);

        if(f.hasErrors()) {
            return badRequest(f.errorsAsJson());
        }

        ManyTagNamesAndFilterDTO dto = f.get();
		Filter filter = Filter.find.byId(dto.filterId);
		if (filter == null) {
			return badRequest("Could not find taggable object with id " + dto.filterId);
		}

		FilterService.addTags(filter, dto.tagNames);
		return ok(Json.toJson(filter));
    }

    public static Result removeTag() {
        JsonNode json = request().body().asJson();
		Form<TagNameAndFilterDTO> f = JsonUtil.getFromJson(TagNameAndFilterDTO.class, json);

        if (f.hasErrors()) {
            return badRequest(f.errorsAsJson());
        }

        TagNameAndFilterDTO dto = f.get();
		Filter filter = Filter.find.byId(dto.filterId);
		if (filter == null) {
			return badRequest("Could not find filter with id " + dto.filterId);
		}

		Tag tag;
		try {
			tag = TagsService.findByName(dto.tagName);
		} catch (NoSuchElementException ex) {
			return badRequest("Could not find tag with name " + dto.tagName);
		}

		FilterService.removeTagFromFilter(filter, tag);
		return ok(Json.toJson(filter));
    }
}
