package se.taggy.tests;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import org.junit.Test;

import se.taggy.tests.note.AddNoteTest;
import se.taggy.tests.note.ListNoteTest;
import se.taggy.tests.note.Note;
import se.taggy.tests.tag.AddTagTest;
import se.taggy.tests.tag.Tag;
import se.taggy.tests.util.TOrJson;
import se.taggy.tests.util.TestUtil;

import com.fasterxml.jackson.databind.JsonNode;

public class TagNoteTest {

	public static JsonNode tagNote(Note note, Tag tag) {
		return tagNote(note, tag.getName());
	}

	public static JsonNode tagNote(Note note, String tag) {
		return tagNote(note.getId(), tag);
	}

	public static JsonNode tagNote(int noteId, String tag) {
		final AtomicReference<JsonNode> asd = new AtomicReference<>();
		TestUtil.put("/notes/" + noteId + "/tag/" + tag, null, (response, responseAsJson) -> {
			asd.set(responseAsJson);
		});
		return asd.get();
	}

	@Test
	public void tagExistingNoteWithNewTag() {
		AddNoteTest.addNoteAndThenRemoveIt("a", "a", note -> {
			tagNote(note, "b");
			
			TOrJson<Note> loadedNote = ListNoteTest.loadNote(note.getId());
			assertThat(loadedNote.getT().getTags().size(), is(note.getTags().size() + 1));
			assertThat(loadedNote.getT().getTags(), hasItems(new Tag("b")));
		});
	}

	@Test
	public void tagExistingNoteWithExistingTag() {
		AddTagTest.createTagAndThenRemoveIt(tag -> {
			AddNoteTest.addNoteAndThenRemoveIt("a", UUID.randomUUID().toString(), note -> {

				tagNote(note, tag);
				TOrJson<Note> loadedNote = ListNoteTest.loadNote(note.getId());
				assertThat(loadedNote.getT().getTags().size(), is(note.getTags().size() + 1));
				assertThat(loadedNote.getT().getTags(), hasItems(tag));
			});
		});
	}

	@Test
	public void tagNonExistingNote() {
		JsonNode response = tagNote(Integer.MAX_VALUE, "asd");
		assertThat(response.get("message").asText(), is(equalTo("no such node")));
	}

	@Test
	public void tagNoteManyTimesWithTheSameTag() {
		AddTagTest.createTagAndThenRemoveIt(tag -> {
			AddNoteTest.addNoteAndThenRemoveIt("a", UUID.randomUUID().toString(), note -> {

				tagNote(note, tag);
				tagNote(note, tag);
				tagNote(note, tag);
				tagNote(note, tag);
				tagNote(note, tag);
				tagNote(note, tag);
				tagNote(note, tag);
				
				TOrJson<Note> loadedNote = ListNoteTest.loadNote(note.getId());
				assertThat(loadedNote.getT().getTags().size(), is(note.getTags().size() + 1));
				assertThat(loadedNote.getT().getTags(), hasItems(tag));
			});
		});
	}
}
