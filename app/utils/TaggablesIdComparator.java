package utils;

import java.util.Comparator;
import models.TaggableObject;

/**
 *
 * @author eriklark
 */
public class TaggablesIdComparator implements Comparator<TaggableObject> {

	@Override
	public int compare(TaggableObject o1, TaggableObject o2) {
		if (o1.id < o2.id) {
			return -1;
		}

		if (o1.id > o2.id) {
			return 1;
		}

		return 0;
	}
}
