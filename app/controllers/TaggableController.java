package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.dto.ManyTagNamesAndTaggableDTO;
import controllers.dto.TagNameAndTaggableDTO;
import controllers.dto.TaggableDTO;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import models.Tag;
import models.TaggableObject;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.TaggableService;
import services.TagsService;
import util.Validation;
import utils.JsonUtil;

/**
 *
 * @author Thinner
 */
public class TaggableController extends Controller {

	/**
	 * Lists all taggables
	 *
	 * @throws JsonProcessingException
	 */
	public static Result list() throws JsonProcessingException {
		return ok(Json.toJson(TaggableObject.all()));
	}

	/**
	 * Creates or updates a taggable
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
			String[] tagNames = JsonUtil.getTagNames(json.findValue("tags"));
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
			tagNames = JsonUtil.getTagNames(tags);
			if (tagNames == null || tagNames.length == 0 || JsonUtil.onlyEmptyStrings(tagNames)) {
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

	@BodyParser.Of(BodyParser.Json.class)
	public static Result remove() {
		JsonNode json = request().body().asJson();
		Form<TaggableDTO> f = JsonUtil.getFromJson(TaggableDTO.class, json);

        if(f.hasErrors()) {
            return badRequest(f.errorsAsJson());
        }

		TaggableDTO dto = f.get();
		TaggableObject taggable = TaggableObject.find.byId(dto.taggableId);
		if (taggable == null) {
			return badRequest("Could not find taggable object with id " + dto.taggableId);
		} else {
			TaggableService.remove(taggable);
			return ok();
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result tag() {
		JsonNode json = request().body().asJson();
        Form<ManyTagNamesAndTaggableDTO> f = JsonUtil.getFromJson(ManyTagNamesAndTaggableDTO.class, json);

        if(f.hasErrors()) {
            return badRequest(f.errorsAsJson());
        }

        ManyTagNamesAndTaggableDTO dto = f.get();
        TaggableObject taggable = TaggableObject.find.byId(dto.taggableId);
        if (taggable == null) {
			return badRequest("Could not find taggable object with id " + dto.taggableId);
		}

        TaggableService.addTags(taggable, dto.tagNames);
        return ok(Json.toJson(taggable));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result untag() {
		JsonNode json = request().body().asJson();
        Form<TagNameAndTaggableDTO> f = JsonUtil.getFromJson(TagNameAndTaggableDTO.class, json);

        if(f.hasErrors()) {
            return badRequest(f.errorsAsJson());
        }

        TagNameAndTaggableDTO dto = f.get();
		TaggableObject taggable = TaggableObject.find.byId(dto.taggableId);
		if (taggable == null) {
			return badRequest("Could not find taggable object with id " + dto.taggableId);
		}

		Tag tag;
		try {
			tag = TagsService.findByName(dto.tagName);
		} catch (NoSuchElementException ex) {
			return badRequest("Could not find tag with name " + dto.tagName);
		}

		TaggableService.removeTagFromTaggable(taggable, tag);
		return ok(Json.toJson(taggable));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result findTagged() {
		JsonNode json = request().body().asJson();
		JsonNode tagNamesJson = json.findValue("tagNames");

		String[] tagNames = JsonUtil.getTagNames(tagNamesJson);

		if(tagNames == null || JsonUtil.onlyEmptyStrings(tagNames)) {
			return badRequest("No tag names given");
		}

		try {
			Collection<Tag> tags = TagsService.findTagsByName(Arrays.asList(tagNames));
			return ok(Json.toJson(TaggableService.findTaggablesTaggedWith(tags)));
		} catch (NoSuchElementException ex) {
			return badRequest(ex.getMessage());
		}
	}
}
