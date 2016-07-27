package se.taggy.tests;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import se.taggy.tests.note.AddNoteTest;
import se.taggy.tests.tag.AddTagTest;
import se.taggy.tests.util.TestUtil;

import com.fasterxml.jackson.databind.node.ArrayNode;

public class CountNotesInTagTest {

	@Test
	public void countNewTag() {
		String tagName = UUID.randomUUID().toString();
		TestUtil.get("/countTags/" + tagName, null, (response, responseAsJson) -> {
			assertTrue(responseAsJson.isArray());
			ArrayNode an = (ArrayNode) responseAsJson;

			assertThat(an.size(), is(1));
			assertThat(an.get(0).get("tag"), is(tagName));
			assertThat(an.get(0).get("count"), is(0));
		});
	}

	@Test
	public void countTagWithNoNotes() {
		AddTagTest.createTagAndThenRemoveIt(tag -> {
			TestUtil.get("/countTags/" + tag.getName(), null, (response, responseAsJson) -> {
				assertTrue(responseAsJson.isArray());
				ArrayNode an = (ArrayNode) responseAsJson;

				assertThat(an.size(), is(1));
				assertThat(an.get(0).get("tag"), is(tag.getName()));
				assertThat(an.get(0).get("count"), is(0));
			});
		});
	}

	@Test
	public void countTagsWithNotes() {
		AddTagTest.createTagAndThenRemoveIt(tag -> {
			AddNoteTest.addNoteAndThenRemoveIt("a", tag, note1 -> {
				AddNoteTest.addNoteAndThenRemoveIt("a", tag, note2 -> {

					TestUtil.get("/countTags/" + tag.getName(), null, (response, responseAsJson) -> {
						assertTrue(responseAsJson.isArray());
						ArrayNode an = (ArrayNode) responseAsJson;

						assertThat(an.size(), is(1));
						assertThat(an.get(0).get("tag"), is(tag.getName()));
						assertThat(an.get(0).get("count"), is(2));
					});
				});
			});
		});
	}
}
