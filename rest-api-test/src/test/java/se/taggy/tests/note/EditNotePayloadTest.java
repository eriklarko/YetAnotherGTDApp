package se.taggy.tests.note;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

import se.taggy.tests.util.TestUtil;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class EditNotePayloadTest {

	@Test
	public void editExistingNote() {
		String tag = UUID.randomUUID().toString();
		AddNoteTest.addNoteAndThenRemoveIt("a", tag, (note) -> {

			String newPayload = UUID.randomUUID().toString();
			ObjectNode json = TestUtil.mapper.createObjectNode();
			json.put("payload", newPayload);

			assertThat(note.getPayload(), is(not(newPayload)));
			TestUtil.put("/notes/" + note.getId(), json, (response, responseAsJson) -> {
				assertThat(note.getId(), is(responseAsJson.get("id")));
				assertThat(responseAsJson.get("payload"), is(newPayload));

				assertThat(note.getTags().size(), is(1));
				assertThat(note.getTags().get(0).getName(), is(tag));
			});
		});
	}

	@Test
	public void editNonExistingNote() {
		ObjectNode json = TestUtil.mapper.createObjectNode();
		json.put("payload", UUID.randomUUID().toString());

		TestUtil.put("/notes/" + Integer.MAX_VALUE, json, (response, responseAsJson) -> {
			assertThat(response.getStatusLine().getStatusCode(), is(422));
			assertThat(responseAsJson.get("message").asText(), is("no such note"));
		});
	}
}
