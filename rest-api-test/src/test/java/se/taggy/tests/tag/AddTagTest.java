package se.taggy.tests.tag;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import se.taggy.tests.util.TestBase;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static se.taggy.tests.tag.RemoveTagTest.removeTag;

public class AddTagTest extends TestBase {

    public static Tag unsafeAddTag(TestBase base, String name) {
        ObjectNode json = mapper.createObjectNode();
        json.put("name", name);

        final AtomicReference<Tag> tag = new AtomicReference<>();
        base.post("/tags", json, (response, responseAsJson) -> {
            try {
                Tag tag1 = mapper.readValue(responseAsJson.toString(), Tag.class);
                tag.set(tag1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return tag.get();
    }

    public static void createTagAndThenRemoveIt(TestBase base, Consumer<Tag> cb) {
        createTagAndThenRemoveIt(base, UUID.randomUUID().toString(), cb);
    }

    public static void createTagAndThenRemoveIt(TestBase base, String tagName, Consumer<Tag> cb) {
        Tag tag = unsafeAddTag(base, tagName);
        cb.accept(tag);
        removeTag(base, tag.getName());
    }

    @Test
    public void testAddTag_uniqueName() {
        String tagName = UUID.randomUUID().toString();

        createTagAndThenRemoveIt(this, tagName, (tag) -> {
            assertThat(tag.getName(), is(equalTo(tagName)));
        });
    }

    @Test
    public void testAddTag_duplicateName() {
        createTagAndThenRemoveIt(this,  (firstTag) -> {
            createTagAndThenRemoveIt(this, firstTag.getName(), secondTag -> {
                assertThat(secondTag.getName(), is(equalTo(firstTag.getName())));
            });
        });
    }
}
