
package search.execution;

import utils.TestUtils;
import com.google.common.collect.Sets;
import java.util.Set;
import models.Note;
import models.Tag;
import org.junit.Assert;
import org.junit.Test;
import search.IdEq;
import utils.PlayIntegrationTest;

/**
 *
 * @author Thinner
 */
public class IdEqTest extends PlayIntegrationTest {

    @Test
    public void testNoMatchingNotes() {
        Tag tagToFind = TestUtils.createTag();
        IdEq ideq = new IdEq(null, tagToFind);

        Set<Note> actual = ideq.execute();

        Assert.assertTrue(actual.isEmpty());
    }

    @Test
    public void testOneNoteOneTag() {
        Tag tagToFind = TestUtils.createTag();
        Note noteToFind = TestUtils.createNote(tagToFind);
        IdEq ideq = new IdEq(null, tagToFind);

        Set<Note> expected = Sets.newHashSet(noteToFind);
        Set<Note> actual = ideq.execute();

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testOneNoteManyTags() {
        Tag tagToFind = TestUtils.createTag();
        Tag random1 = TestUtils.createTag();
        Tag random2 = TestUtils.createTag();
        Tag random3 = TestUtils.createTag();

        Note noteToFind = TestUtils.createNote(random1, random2, tagToFind, random3);
        IdEq ideq = new IdEq(null, tagToFind);

        Set<Note> expected = Sets.newHashSet(noteToFind);
        Set<Note> actual = ideq.execute();

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testManyNotesOneTag() {
        Tag tagToFind = TestUtils.createTag();
        IdEq ideq = new IdEq(null, tagToFind);

        Set<Note> expected = TestUtils.createNotes(3, tagToFind);
        Set<Note> actual = TestUtils.sort(ideq.execute());

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testManyNotesManyTags() {
        Tag tagToFind = TestUtils.createTag();
        Tag random1 = TestUtils.createTag();
        Tag random2 = TestUtils.createTag();
        Tag random3 = TestUtils.createTag();
        IdEq ideq = new IdEq(null, tagToFind);

        Set<Note> expected = TestUtils.createNotes(3, random1, random2, tagToFind, random3);
        Set<Note> actual = TestUtils.sort(ideq.execute());

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }


}
