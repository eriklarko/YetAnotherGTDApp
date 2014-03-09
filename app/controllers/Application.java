package controllers;

import models.Filter;
import play.mvc.Controller;
import play.mvc.Result;
import services.FilterService;
import views.html.combinedView;
import views.html.filterList;
import views.html.viewFilter;

public class Application extends Controller {

	public static Result filterList() {
		return ok(filterList.render());
	}

	public static Result combined(Long filterId) {
		Filter filter = FilterService.instance().byId(filterId);
		return ok(combinedView.render(filter));
	}

	public static Result viewAllNotes() {
		return ok(viewFilter.render(null));
	}

	public static Result viewFilter(Long id) {
		Filter filter = FilterService.instance().byId(id);
		if (filter == null) {
			return badRequest("No filter with id " + id + " found");
		}
		return ok(viewFilter.render(filter));
	}
}
