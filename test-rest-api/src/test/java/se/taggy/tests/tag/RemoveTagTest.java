package se.taggy.tests.tag;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import se.taggy.tests.TagNoteTest;
import se.taggy.tests.filter.AddFilterTest;
import se.taggy.tests.filter.Filter;
import se.taggy.tests.filter.ListFiltersTest;
import se.taggy.tests.filter.RemoveFilterTest;
import se.taggy.tests.note.AddNoteTest;
import se.taggy.tests.note.ListNoteTest;
import se.taggy.tests.note.Note;
import se.taggy.tests.util.TOrJson;
import se.taggy.tests.util.TestUtil;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class RemoveTagTest {

	public static JsonNode removeTag(String tagName) {
		final AtomicReference<JsonNode> response = new AtomicReference<>();
		TestUtil.sendRequest("/tags/" + tagName, TestUtil.Method.DELETE, (entity, json) -> {
			response.set(json);
		});
		return response.get();
	}

	@Test
	public void removeNonExistingTag() {
		JsonNode jsonNode = removeTag(UUID.randomUUID().toString());
		assertThat(jsonNode.get("message").asText(), is(equalTo("no such tag")));
	}

	@Test
	public void removeUnusedTag() {
		Tag tag = AddTagTest.unsafeAddTag(UUID.randomUUID().toString());
		JsonNode response = removeTag(tag.getName());

		assertThat(response.get("message").asText(), is(equalTo("ok")));
	}

	@Test
	public void removeTagUsedByNote() {
		AddTagTest.createTagAndThenRemoveIt(tag -> {
			Note note = AddNoteTest.unsafeCreateNote();
			TagNoteTest.tagNote(note, tag);

			JsonNode response = removeTag(tag.getName());
			assertThat(response.get("message").asText(), is(equalTo("ok")));

			TOrJson<Note> loadedNote = ListNoteTest.loadNote(note.getId());
			assertThat(loadedNote.getT().getTags(), not(contains(tag)));
		});
	}

	@Test
	public void removeTagUsedByFilter() {
		AddTagTest.createTagAndThenRemoveIt(tag -> {

			ObjectNode searchTree = TestUtil.mapper.createObjectNode();
			searchTree.put("type", "nameeq");
			searchTree.put("name", tag.getName());

			safelyAddFilterWithSearchTree(searchTree, filter -> {

				JsonNode response = removeTag(tag.getName());
				assertThat(response.get("message").asText(), is(equalTo("ok")));

				TOrJson<Filter> loadedFilter = ListFiltersTest.loadFilter(filter.getId());

				assertTrue(loadedFilter.hasT()); // The filter should not have been removed
				assertThat(loadedFilter.getT().getId(), is(filter.getId()));
				assertThat(loadedFilter.getT().getName(), is(filter.getName()));
				assertThat(loadedFilter.getT().getSearchTree(), is(not(filter.getSearchTree()))); // The search tree should be updated
			});
		});
	}

	private void safelyAddFilterWithSearchTree(JsonNode searchTree, Consumer<Filter> cb) {
		Filter filter = null;
		try {
			filter = AddFilterTest.unsafeAddFilter("", searchTree);
			cb.accept(filter);
		} finally {
			if (filter != null) {
				RemoveFilterTest.removeFilter(filter.getId());
			}
		}
	}

	@Test
	public void removeTagUsedByNoteAndFilter() {
		AddTagTest.createTagAndThenRemoveIt(tag -> {

			AddNoteTest.addNoteAndThenRemoveIt(UUID.randomUUID().toString(), tag, note -> {
				ObjectNode searchTree = TestUtil.mapper.createObjectNode();
				searchTree.put("type", "nameeq");
				searchTree.put("name", tag.getName());

				safelyAddFilterWithSearchTree(searchTree, filter -> {
					JsonNode response = removeTag(tag.getName());
					assertThat(response.get("message").asText(), is(equalTo("ok")));

					TOrJson<Note> loadedNote = ListNoteTest.loadNote(note.getId());
					assertTrue(loadedNote.hasT()); // The filter should not have been removed
					assertThat(loadedNote.getT().getId(), is(note.getId()));
					assertThat(loadedNote.getT().getPayload(), is(note.getPayload()));
					assertThat(loadedNote.getT().getTags().size(), is(not(note.getTags().size()))); // The tag list should not be the same, though it should be the same as before without the removed tag...
					// TODO: Verify that the tag list is the same as before but without the removed tag

					TOrJson<Filter> loadedFilter = ListFiltersTest.loadFilter(filter.getId());
					assertTrue(loadedFilter.hasT()); // The filter should not have been removed
					assertThat(loadedFilter.getT().getId(), is(filter.getId()));
					assertThat(loadedFilter.getT().getName(), is(filter.getName()));
					assertThat(loadedFilter.getT().getSearchTree(), is(not(filter.getSearchTree()))); // The search tree should be updated
				});
			});
		});
	}
}
