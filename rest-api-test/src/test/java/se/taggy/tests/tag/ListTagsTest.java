package se.taggy.tests.tag;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import se.taggy.tests.util.TestUtil;

import com.fasterxml.jackson.databind.node.ArrayNode;


public class ListTagsTest {

	@Test
	public void testWithExistingTags() {
		AddTagTest.createTagAndThenRemoveIt(tag -> {
			TestUtil.get("/tags", null, (response, responseAsJson) -> {
				assertTrue(responseAsJson.isArray());

				ArrayNode an = (ArrayNode) responseAsJson;
				assertThat(an.size(), is(1));
				assertThat(an.get("name"), is(tag.getName()));
			});
		});
	}

	@Test
	public void testWithNoExistingTags() {
		TestUtil.get("/tags", null, (response, responseAsJson) -> {
			assertTrue(responseAsJson.isArray());
			ArrayNode an = (ArrayNode) responseAsJson;
			assertThat(an.size(), is(0));
		});
	}
}
