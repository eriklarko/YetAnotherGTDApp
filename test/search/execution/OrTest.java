package search.execution;

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
        Tag t1 = Util.createTag();
        Tag t2 = Util.createTag();

        Collection<Node> literals = Arrays.<Node>asList(
                new IdEq(t1.id),
                new IdEq(t2.id)
        );
        Or or = new Or(literals);

        Note n1 = Util.createNote(t1);
        Note n2 = Util.createNote(t2);

        SortedSet<Note> expected = Util.sort(n1, n2);
        SortedSet<Note> actual = Util.sort(or.execute());

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }
}
