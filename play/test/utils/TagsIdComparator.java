package utils;

import java.util.Comparator;
import models.Tag;

/**
 *
 * @author eriklark
 */
public class TagsIdComparator implements Comparator<Tag>{

	@Override
	public int compare(Tag o1, Tag o2) {
		return o1.id.compareTo(o2.id);
	}

}
