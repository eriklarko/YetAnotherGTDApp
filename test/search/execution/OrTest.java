package search.execution;

import utils.TestUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.SortedSet;
import models.Note;
import models.Tag;
import org.junit.Assert;
import org.junit.Test;
import search.IdEq;
import search.Node;
import search.Or;
import utils.PlayIntegrationTest;

/**
 *
 * @author Thinner
 */
public class OrTest extends PlayIntegrationTest {

    @Test
    public void testSimple() {
        Tag t1 = TestUtils.createTag();
        Tag t2 = TestUtils.createTag();

        Collection<Node> literals = Arrays.<Node>asList(
                new IdEq(t1),
                new IdEq(t2)
        );
        Or or = new Or(literals);

        Note n1 = TestUtils.createNote(t1);
        Note n2 = TestUtils.createNote(t2);

        SortedSet<Note> expected = TestUtils.sort(n1, n2);
        SortedSet<Note> actual = TestUtils.sort(or.execute());

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }
}
