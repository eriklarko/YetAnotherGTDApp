package controllers;

import java.util.Arrays;
import models.Filter;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.filterList;
import views.html.viewFilter;

public class Application extends Controller {

    public static Result filterList() {
        return ok(filterList.render());
    }

    public static Result viewAllNotes() {
        return ok(viewFilter.render(null));
    }

	public static Result viewFilter(Long id) {
		Filter filter = Filter.find.byId(id);
		if (filter == null) {
			return badRequest("No filter with id " + id + " found");
		}

		System.out.println("Tags in filter: " + Arrays.deepToString(filter.tags.toArray()));
        return ok(viewFilter.render(filter));
    }
}
