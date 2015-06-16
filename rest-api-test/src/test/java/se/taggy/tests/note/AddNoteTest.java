package se.taggy.tests.note;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

import se.taggy.tests.tag.AddTagTest;
import se.taggy.tests.tag.Tag;
import se.taggy.tests.util.TestUtil;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AddNoteTest {

	public static Note unsafeCreateNote() {
		return unsafeCreateNote(UUID.randomUUID().toString());
	}

	public static Note unsafeCreateNote(String body) {
		return unsafeCreateNote(UUID.randomUUID().toString(), UUID.randomUUID().toString());
	}

	public static Note unsafeCreateNote(String payload, String... tags) {
		ObjectNode root = TestUtil.mapper.createObjectNode();
		root.put("payload", payload);
		ArrayNode tagsJson = root.putArray("tags");
		for (String tag : tags) {
			tagsJson.add(tag);
		}

		final AtomicReference<Note> n = new AtomicReference<>();
		TestUtil.sendRequest("/notes", root, TestUtil.Method.POST, (entity, body) -> {
			try {
				Note note = TestUtil.mapper.readValue(body.toString(), Note.class);
				n.set(note);
			} catch (IOException e) {
				throw new RuntimeException("Json: " + body.toString(), e);
			}
		});

		return n.get();
	}

	public static void addNoteAndThenRemoveIt(String payload, String tagName, Consumer<Note> cb) {
		Tag tag = new Tag();
		tag.setName(tagName);
		addNoteAndThenRemoveIt(payload, tag, cb);
	}

	public static void addNoteAndThenRemoveIt(String payload, Tag tag, Consumer<Note> cb) {
		Note n = null;
		try {
			n = unsafeCreateNote(payload, tag.getName());
			cb.accept(n);
		} finally {
			if (n != null) {
				RemoveNoteTest.removeNote(n.getId());
			}
		}
	}

	@Test
	public void addNoteWithExistingTag() {
		String payload = UUID.randomUUID().toString();
		AddTagTest.createTagAndThenRemoveIt(tag -> {
			addNoteAndThenRemoveIt(payload, tag, note -> {
				assertThat(note.getPayload(), is(payload));
				assertThat(note.getTags().size(), is(1));
				assertThat(note.getTags().get(0).getName(), is(tag.getName()));
			});
		});
	}

	@Test
	public void addNoteWithNewTag() {
		String payload = UUID.randomUUID().toString();
		String tag = UUID.randomUUID().toString();

		addNoteAndThenRemoveIt(payload, tag, note -> {
			assertThat(note.getPayload(), is(payload));
			assertThat(note.getTags().size(), is(1));
			assertThat(note.getTags().get(0).getName(), is(tag));
		});
	}

	@Test
	public void addNoteWithMultipleTags() {
		String payload = UUID.randomUUID().toString();
		String tag1 = UUID.randomUUID().toString();
		String tag2 = UUID.randomUUID().toString();
		String tag3 = UUID.randomUUID().toString();

		Note note = null;
		try {
			note = unsafeCreateNote(payload, tag1, tag2, tag3);
			assertThat(note.getPayload(), is(payload));
			assertThat(note.getTags().size(), is(3));
			
			List<String> tagNames = note.getTags().stream().map(tag -> tag.getName()).collect(Collectors.toList());
			assertThat(tagNames, hasItems(tag1, tag2, tag3));
		} finally {
			if (note != null) {
				RemoveNoteTest.removeNote(note.getId());
			}
		}
	}

	@Test
	public void addTwoIdenticalNotes() {
		String payload = UUID.randomUUID().toString();
		String tag1 = UUID.randomUUID().toString();

		addNoteAndThenRemoveIt(payload, tag1, note1 -> {
			addNoteAndThenRemoveIt(payload, tag1, note2 -> {
				assertThat(note1.getPayload(), is(note2.getPayload()));
				
				assertThat(note1.getTags().size(), is(note2.getTags().size()));
				assertThat(note1.getTags(), hasItems(note2.getTags().toArray(new Tag[0])));

				assertThat(note1.getId(), is(not(note2.getId())));
			});
		});
	}
}
