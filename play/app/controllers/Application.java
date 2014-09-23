package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import models.Filter;
import models.Note;
import models.Tag;
import play.mvc.Controller;
import play.mvc.Result;
import search.And;
import search.IdEq;
import search.Node;
import search.Not;
import services.FilterService;
import services.NoteService;
import services.TagService;
import views.html.searchView;
import views.html.showFilter;
import views.html.tagsList;
import views.html.tagList;
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
