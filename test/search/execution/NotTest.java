package search.execution;

import java.util.SortedSet;
import models.Note;
import models.Tag;
import org.junit.Assert;
import org.junit.Test;
import search.IdEq;
import search.Not;
import utils.PlayIntegrationTest;

/**
 *
 * @author Thinner
 */
public class NotTest extends PlayIntegrationTest {

    @Test
    public void twoNotes() {
        Tag t1 = Util.createTag();
        Tag t2 = Util.createTag();

        Note noteToFind = Util.createNote(t1);
        Note n2 = Util.createNote(t2);

        Not not = new Not(new IdEq(t2));

        SortedSet<Note> expected = Util.sort(noteToFind);
        SortedSet<Note> actual = Util.sort(not.execute());

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }
}
