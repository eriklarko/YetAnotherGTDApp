package se.taggy.tests;

import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import se.taggy.tests.note.AddNoteTest;
import se.taggy.tests.note.ListNoteTest;
import se.taggy.tests.note.Note;
import se.taggy.tests.tag.Tag;
import se.taggy.tests.util.TOrJson;
import se.taggy.tests.util.TestUtil;

import com.fasterxml.jackson.databind.JsonNode;

public class UntagNoteTest {

	public static JsonNode untagNote(Note note, Tag tag) {
		return untagNote(note, tag.getName());
	}

	public static JsonNode untagNote(Note note, String tag) {
		return untagNote(note.getId(), tag);
	}

	public static JsonNode untagNote(int noteId, String tag) {
		final AtomicReference<JsonNode> asd = new AtomicReference<>();
		TestUtil.delete("/notes/" + noteId + "/tag/" + tag, (response, responseAsJson) -> {
			asd.set(responseAsJson);
		});
		return asd.get();
	}

	@Test
	public void testRemoveTagFromNote() {
		String tagName = "asd";
		AddNoteTest.addNoteAndThenRemoveIt("asd", tagName, note -> {
			TagNoteTest.tagNote(note, tagName + "asd");
			untagNote(note, tagName);

			TOrJson<Note> loadedNote = ListNoteTest.loadNote(note.getId());
			assertTrue(loadedNote.hasT());
			assertThat(loadedNote.getT().getTags().size(), is(1));
			assertThat(loadedNote.getT().getTags(), not(hasItems(new Tag(tagName))));
		});
	}

	@Test
	public void testRemoveTagThatWasNotThere() {
		String tagName = "asd";
		AddNoteTest.addNoteAndThenRemoveIt("asd", tagName, note -> {
			untagNote(note, tagName + "asd");

			TOrJson<Note> loadedNote = ListNoteTest.loadNote(note.getId());
			assertTrue(loadedNote.hasT());
			assertThat(loadedNote.getT().getTags().size(), is(1));
			assertThat(loadedNote.getT().getTags(), hasItems(new Tag(tagName)));
		});
	}

	@Test
	public void testRemoveTagFromMissingNote() {
		JsonNode response = untagNote(Integer.MAX_VALUE, "asd");
		assertThat(response.get("message").asText(), is(equalTo("no such node")));
	}

	@Test
	public void testRemoveLastTag() {
		String tagName = "asd";
		AddNoteTest.addNoteAndThenRemoveIt("asd", tagName, note -> {
			untagNote(note, tagName);

			TOrJson<Note> loadedNote = ListNoteTest.loadNote(note.getId());
			assertTrue(loadedNote.hasT());
			assertThat(loadedNote.getT().getTags().size(), is(0));
		});
	}
}
