package search.execution;

import java.util.ArrayList;
import utils.TestUtils;
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
        Tag t1 = TestUtils.createTag();
        Tag t2 = TestUtils.createTag();
        Tag t3 = TestUtils.createTag();

		Collection<Node> literals = new ArrayList<>();
		And and = new And(null, literals);
        literals.addAll(Arrays.<Node>asList(
				new IdEq(and, t1),
				new IdEq(and, t2),
				new IdEq(and, t3)
		));

        SortedSet<Note> expected = TestUtils.createNotes(3, t1, t2, t3);
        SortedSet<Note> actual = TestUtils.sort(and.execute());

        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
	}
}
