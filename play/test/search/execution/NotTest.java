package search.execution;

import utils.TestUtils;
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
        Tag t1 = TestUtils.createTag();
        Tag t2 = TestUtils.createTag();

        Note noteToFind = TestUtils.createNote(t1);
        Note n2 = TestUtils.createNote(t2);

        Not not = new Not(null);
        not.setChild(new IdEq(not, t2));

        SortedSet<Note> expected = TestUtils.sort(noteToFind);
        SortedSet<Note> actual = TestUtils.sort(not.execute());

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }
}
