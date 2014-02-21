package search.execution;

import java.util.Arrays;
import java.util.Collection;
import junit.framework.Assert;
import org.junit.Test;
import search.And;
import search.IdEq;
import search.Node;

/**
 *
 * @author eriklark
 */
public class AndTest {

	@Test
	public void testSingleChain() {
		Collection<Node<String>> literals = Arrays.<Node<String>>asList(
				new IdEq<Long>("id", 1L),
				new IdEq<Long>("id", 2L),
				new IdEq<Long>("id", 3L)
		);
		And and = new And(literals);

		String expected = "(id = 1 AND id = 2 AND id = 3)";
		String actual = and.execute();

		Assert.assertEquals(expected, actual);
	}
}
