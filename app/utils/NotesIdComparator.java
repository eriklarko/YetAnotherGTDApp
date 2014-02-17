package utils;

import java.util.Comparator;
import models.Note;

/**
 *
 * @author eriklark
 */
public class NotesIdComparator implements Comparator<Note> {

	@Override
	public int compare(Note o1, Note o2) {
		if (o1.id < o2.id) {
			return -1;
		}

		if (o1.id > o2.id) {
			return 1;
		}

		return 0;
	}
}
