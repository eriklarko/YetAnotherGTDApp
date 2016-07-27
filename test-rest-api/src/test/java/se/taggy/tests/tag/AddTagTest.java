package se.taggy.tests.tag;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import org.junit.Test;

import se.taggy.tests.util.TestUtil;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class AddTagTest {

    public static Tag unsafeAddTag(String name) {
        ObjectNode json = TestUtil.mapper.createObjectNode();
        json.put("name", name);

        final AtomicReference<Tag> tag = new AtomicReference<>();
        TestUtil.post("/tags", json, (response, responseAsJson) -> {
            try {
                Tag tag1 = TestUtil.mapper.readValue(responseAsJson.toString(), Tag.class);
                tag.set(tag1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return tag.get();
    }

    public static void createTagAndThenRemoveIt(Consumer<Tag> cb) {
        createTagAndThenRemoveIt(UUID.randomUUID().toString(), cb);
    }

    public static void createTagAndThenRemoveIt(String tagName, Consumer<Tag> cb) {
        Tag tag = unsafeAddTag(tagName);
        cb.accept(tag);
        RemoveTagTest.removeTag(tag.getName());
    }

    @Test(expected = RuntimeException.class)
    public void testAddTag_nullName() {
        unsafeAddTag(null);
    }

    @Test
    public void testAddTag_uniqueName() {
        String tagName = UUID.randomUUID().toString();

        createTagAndThenRemoveIt(tagName, (tag) -> {
            assertThat(tag.getName(), is(equalTo(tagName)));
        });
    }

    @Test
    public void testAddTag_duplicateName() {
        createTagAndThenRemoveIt((firstTag) -> {
            createTagAndThenRemoveIt(firstTag.getName(), secondTag -> {
                assertThat(secondTag.getName(), is(equalTo(firstTag.getName())));
            });
        });
    }
}
