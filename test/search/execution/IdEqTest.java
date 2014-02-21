
package search.execution;

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

    @Test(expected = IllegalArgumentException.class)
    public void testTagNotFound() {
        IdEq ideq = new IdEq(Long.MIN_VALUE);
        ideq.execute();
    }

    @Test
    public void testNoMatchingNotes() {
        Tag tagToFind = Util.createTag();
        IdEq ideq = new IdEq(tagToFind.id);

        Set<Note> actual = ideq.execute();

        Assert.assertTrue(actual.isEmpty());
    }

    @Test
    public void testOneNoteOneTag() {
        Tag tagToFind = Util.createTag();
        Note noteToFind = Util.createNote(tagToFind);
        IdEq ideq = new IdEq(tagToFind.id);

        Set<Note> expected = Sets.newHashSet(noteToFind);
        Set<Note> actual = ideq.execute();

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testOneNoteManyTags() {
        Tag tagToFind = Util.createTag();
        Tag random1 = Util.createTag();
        Tag random2 = Util.createTag();
        Tag random3 = Util.createTag();

        Note noteToFind = Util.createNote(random1, random2, tagToFind, random3);
        IdEq ideq = new IdEq(tagToFind.id);

        Set<Note> expected = Sets.newHashSet(noteToFind);
        Set<Note> actual = ideq.execute();

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testManyNotesOneTag() {
        Tag tagToFind = Util.createTag();
        IdEq ideq = new IdEq(tagToFind.id);

        Set<Note> expected = Util.createNotes(3, tagToFind);
        Set<Note> actual = Util.sort(ideq.execute());

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testManyNotesManyTags() {
        Tag tagToFind = Util.createTag();
        Tag random1 = Util.createTag();
        Tag random2 = Util.createTag();
        Tag random3 = Util.createTag();
        IdEq ideq = new IdEq(tagToFind.id);

        Set<Note> expected = Util.createNotes(3, random1, random2, tagToFind, random3);
        Set<Note> actual = Util.sort(ideq.execute());

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }


}
