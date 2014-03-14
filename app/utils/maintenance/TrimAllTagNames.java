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
			TagService.instance().updateName(tag, tag.name.trim());
			TagService.instance().findByName(tag.name);
		};
	}
}
