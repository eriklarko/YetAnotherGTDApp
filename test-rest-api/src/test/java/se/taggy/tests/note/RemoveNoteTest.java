package se.taggy.tests.note;

import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import org.junit.Assert;
import org.junit.Test;

import se.taggy.tests.util.TOrJson;
import se.taggy.tests.util.TestUtil;

import com.fasterxml.jackson.databind.JsonNode;

public class RemoveNoteTest {

	public static JsonNode removeNote(int noteId) {
		final AtomicReference<JsonNode> response = new AtomicReference<>();
        TestUtil.sendRequest("/notes/" + noteId, TestUtil.Method.DELETE, (entity, json) -> {
            response.set(json);
        });
        return response.get();
	}

	@Test
	public void testRemoveExistingNote() {
		Note toRemove = AddNoteTest.unsafeCreateNote();

		JsonNode json = removeNote(toRemove.getId());
		assertThat(json.get("message").asText(), is(equalToIgnoringCase("ok")));

		TOrJson<Note> loadedNote = ListNoteTest.loadNote(toRemove.getId());
		Assert.assertFalse("loadNote returned a valid note. The note is not removed", loadedNote.hasT());
	}

	@Test
	public void testRemoveNonExistingNote() {
		TestUtil.delete("/notes/" + Integer.MAX_VALUE, (response, responseAsJson) -> {
			assertThat(response.getStatusLine().getStatusCode(), is(422));
			assertThat(responseAsJson.get("message").asText(), is("no such note"));
		});
	}
}
