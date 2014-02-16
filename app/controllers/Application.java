package controllers;

import models.Filter;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.filterList;
import views.html.viewFilter;

public class Application extends Controller {

    public static Result filterList() {
        return ok(filterList.render());
    }

    public static Result viewAllTaggables() {
        return ok(viewFilter.render(null));
    }

	public static Result viewFilter(Long id) {
		Filter filter = Filter.find.byId(id);
		if (filter == null) {
			return badRequest("No filter with id " + id + " found");
		}

        return ok(viewFilter.render(filter));
    }
}
