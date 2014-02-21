package search;

import java.util.Set;
import models.Note;

/**
 *
 * @author eriklark
 */
public interface Node {

	abstract Set<Note> execute();
}
