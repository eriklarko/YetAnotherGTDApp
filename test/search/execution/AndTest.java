package search.execution;

import java.util.Arrays;
import java.util.Collection;
import java.util.SortedSet;
import models.Note;
import models.Tag;
import org.junit.Assert;
import org.junit.Test;
import search.And;
import search.IdEq;
import search.Node;
import utils.PlayIntegrationTest;

/**
 *
 * @author eriklark
 */
public class AndTest extends PlayIntegrationTest {

	@Test
	public void testSingleChain() {
        Tag t1 = Util.createTag();
        Tag t2 = Util.createTag();
        Tag t3 = Util.createTag();

		Collection<Node> literals = Arrays.<Node>asList(
				new IdEq(t1),
				new IdEq(t2),
				new IdEq(t3)
		);
		And and = new And(literals);

        SortedSet<Note> expected = Util.createNotes(3, t1, t2, t3);
        SortedSet<Note> actual = Util.sort(and.execute());

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
	}
}
