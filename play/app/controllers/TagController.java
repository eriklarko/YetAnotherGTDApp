package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import models.Note;
import models.Tag;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import static play.mvc.Controller.request;
import play.mvc.Result;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;
import search.And;
import search.IdEq;
import search.Node;
import search.Not;
import services.NoteService;
import services.TagService;
import views.html.tagList;

/**
 *
 * @author Thinner
 */
public class TagController extends Controller {

	public static Result list() throws JsonProcessingException {
		return ok(Json.toJson(TagService.instance().getAllCurrentUsersTags()));
	}

	public static Result listWithNumberOfNotes() {
		boolean removeArchived = true;

		Tag archiveTag = TagService.instance().getArchiveTag();

		ArrayNode toReturn = new ArrayNode(JsonNodeFactory.instance);
		Set<Tag> allTags = TagService.instance().getAllCurrentUsersTags();
		for (Tag tag : allTags) {
			Node searchTree = new IdEq(null, tag);
			if (!Objects.equals(tag.id, archiveTag.id) && removeArchived) {
				searchTree = new And(null, Arrays.asList(new Node[]{
					new Not(null, new IdEq(null, archiveTag)),
					searchTree
				}));
			}
			ObjectNode tagJson = (ObjectNode) Json.toJson(tag);
			tagJson.put("numNotes", Json.toJson(searchTree.execute().size()));

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

    public static Result listNotesWithTag(Long tagId) {
		Tag tag = TagService.instance().byId(tagId);
		if (tag == null) {
			return badRequest("Unknown tag");
		}

		Tag archiveTag = TagService.instance().getArchiveTag();

		Collection<Note> notes;
		if (Objects.equals(tag.id, archiveTag.id)) {
			notes = NoteService.instance().findNotesWithTag(tag);
		} else {
            Collection<Node> children = new ArrayList<>();
			Node searchTree = new And(null, children);

            Not notArchive = new Not(searchTree);
            notArchive.setChild(new IdEq(notArchive, archiveTag));

            children.add(notArchive);
            children.add(new IdEq(searchTree, tag));

			notes = searchTree.execute();
		}

		return ok(tagList.render(tag, notes));
	}
}
