package controllers;

import models.Filter;
import models.Tag;
import play.mvc.Controller;
import play.mvc.Result;
import services.FilterService;
import services.NoteService;
import services.TagService;
import views.html.notesList;
import views.html.showFilter;

public class Application extends Controller {

	public static Result showFilter(Long filterId) {
		Filter filter = FilterService.instance().byId(filterId);
		return ok(showFilter.render(filter));
	}

	public static Result listNotesWithTag(Long tagId) {
		Tag tag = TagService.instance().byId(tagId);
		return ok(notesList.render(NoteService.instance().findNotesWithTag(tag)));
	}

	public static Result viewAllNotes() {
		return ok(notesList.render(NoteService.instance().findAllCurrentUsersNotes()));
	}

	public static Result searchView(String query) {
		return ok();
	}
}
