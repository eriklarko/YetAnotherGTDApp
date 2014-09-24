package controllers;

import java.util.Collection;
import models.Filter;
import models.Note;
import models.Tag;
import play.mvc.Controller;
import play.mvc.Result;
import services.FilterService;
import services.NoteService;
import services.TagService;
import views.html.searchView;
import views.html.showFilter;
import views.html.tagsList;
import views.html.notesList;

public class Application extends Controller {

	public static Result home() {
		return ok(showFilter.render(null));
	}

	public static Result showFilter(Long filterId) {
		Filter filter = FilterService.instance().byId(filterId);
		return ok(showFilter.render(filter));
	}

	public static Result listTags() {
		return ok(tagsList.render());
	}

	public static Result viewAllNotes() {
		return ok(notesList.render(NoteService.instance().findAllCurrentUsersNotes()));
	}

	public static Result searchView(String query) {
		query = "%" + query + "%";
		Collection<Note> notes = NoteService.instance().findNotesWithPayloadMatchingQuery(query);
		Collection<Tag> tags = TagService.instance().findTagsWithNameMatchingQuery(query);

		return ok(searchView.render(query, tags, notes));
	}
}
