package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import utils.maintenance.TrimAllTagNames;

/**
 *
 * @author eriklark
 */
public class Maintenance extends Controller {
	
	public static Result doMaintenance(String which) {
		switch (which) {
			case "trim-tag-names":
				new TrimAllTagNames().run();
				return ok();

			default:
				return badRequest("I didn't understand that: " + which);
		}
	}
}
