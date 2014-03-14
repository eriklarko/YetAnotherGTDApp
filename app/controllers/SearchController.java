package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Collection;
import models.Note;
import models.Tag;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.NoteService;
import services.TagService;

/**
 *
 * @author eriklark
 */
public class SearchController extends Controller {

	public static Result findPayloadsAndTagNames(String query) {
		query = "%" + query + "%";

		Collection<Note> notes = NoteService.instance().findNotesWithPayloadMatchingQuery(query);
		Collection<Tag> tags = TagService.instance().findTagsWithNameMatchingQuery(query);

		ObjectNode toReturn = Json.newObject();
		toReturn.put("notes", Json.toJson(notes));
		toReturn.put("tags", Json.toJson(tags));

		return ok(toReturn);
	}

}
