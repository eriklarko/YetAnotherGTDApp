package se.taggy.tests.tag;

import com.fasterxml.jackson.databind.JsonNode;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import se.taggy.tests.TagNoteTest;
import se.taggy.tests.note.Note;
import se.taggy.tests.util.TestBase;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.IsEqual.equalTo;
import static se.taggy.tests.note.AddNoteTest.unsafeCreateNote;
import static se.taggy.tests.note.ListNoteTest.loadNote;

public class RemoveTagTest extends TestBase {

    public static JsonNode removeTag(TestBase base, String tagName) {
        final AtomicReference<JsonNode> response = new AtomicReference<>();
        base.sendRequest("/tags/" + tagName, Method.DELETE, (entity, json) -> {
            response.set(json);
        });
        return response.get();
    }

    @Test
    public void removeNonExistingTag() {
        JsonNode jsonNode = removeTag(this, UUID.randomUUID().toString());
        assertThat(jsonNode.get("message").asText(), is(equalTo("no such tag")));
    }

    @Test
    public void removeUnusedTag() {
        Tag tag = AddTagTest.unsafeAddTag(this, UUID.randomUUID().toString());
        JsonNode response = removeTag(this, tag.getName());

        assertThat(response.get("message").asText(), is(equalTo("ok")));
    }

    @Test
    public void removeTagUsedByNote() {
        AddTagTest.createTagAndThenRemoveIt(this, tag -> {
            Note note = unsafeCreateNote(this);
            TagNoteTest.tagNote(note, tag);

            JsonNode response = removeTag(this, tag.getName());
            assertThat(response.get("message").asText(), is(equalTo("ok")));

            note = loadNote(this, note.getId());
            assertThat(note.getTags(), not(contains(tag)));
        });
    }

    @Test
    public void removeTagUsedByFilter() {
        Assert.fail("Not implemented yet");
    }

    @Test
    public void removeTagUsedBygNoteAndFilter() {
        Assert.fail("Not implemented yet");
    }
}
