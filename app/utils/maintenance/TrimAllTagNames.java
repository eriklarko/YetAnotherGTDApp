package utils.maintenance;

import models.Tag;
import services.TagService;

/**
 *
 * @author eriklark
 */
public class TrimAllTagNames implements Runnable {

	@Override
	public void run() {
		for (Tag tag : TagService.instance().getAllCurrentUsersTags()) {
			System.out.println("Trimming tag " + tag);
			tag = TagService.instance().updateName(tag, tag.getName().trim());
			TagService.instance().findByName(tag.getName());
		}
	}
}
