package se.taggy.tests.note;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import se.taggy.tests.util.TOrJson;
import se.taggy.tests.util.TestUtil;

import com.fasterxml.jackson.databind.node.ArrayNode;

public class ListNoteTest {

	public static TOrJson<Note> loadNote(int noteId) {
		final AtomicReference<TOrJson> note = new AtomicReference<>();
		TestUtil.get("/notes/" + noteId, null, (response, responseAsJson) -> {
			try {
				Note n = TestUtil.mapper.readValue(responseAsJson.toString(), Note.class);
				note.set(new TOrJson<>(n));
			} catch (IOException ex) {
				note.set(new TOrJson<>(responseAsJson));
			}
		});

		return note.get();
	}

	@Test
	public void testListExistingNote() {
		AddNoteTest.addNoteAndThenRemoveIt("a", "b", note -> {
			TOrJson<Note> loadedNote = loadNote(note.getId());
			assertTrue(loadedNote.hasT());

			assertThat(loadedNote.getT().getPayload(), is("a"));
			assertThat(loadedNote.getT().getTags().size(), is(1));
			assertThat(loadedNote.getT().getTags().get(0).getName(), is("b"));
		});
	}

	@Test
	public void testListNonExistingNote() {
		TOrJson<Note> note = loadNote(Integer.MAX_VALUE);
		assertFalse(note.hasT());
	}

	@Test
	public void listAllNotes_WithExistingNotes() {
		AddNoteTest.addNoteAndThenRemoveIt("a", "a", noteA -> {
			AddNoteTest.addNoteAndThenRemoveIt("b", "b", noteB -> {
				AddNoteTest.addNoteAndThenRemoveIt("c", "c", noteC -> {

					TestUtil.get("/notes", null, (response, responseAsJson) -> {
						// TODO: Make sure three notes are returned
						// TODO: Make sure the correct three notes are returned :)
					});
				});
			});
		});
	}

	@Test
	public void listAllNotes_WithoutExistingNotes() {
		TestUtil.get("/notes", null, (response, responseAsJson) -> {
			assertTrue(responseAsJson.isArray());
			ArrayNode an = (ArrayNode) responseAsJson;
			assertThat(an.size(), is(0));
		});
	}
}
