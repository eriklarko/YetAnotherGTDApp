package se.taggy.tests.filter;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

import se.taggy.tests.util.TestUtil;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AddFilterTest {

    /**
     * Does not clean up after itself. Use with caution!
     */
    public static Filter unsafeAddFilter() {
        return unsafeAddFilter(UUID.randomUUID().toString());
    }

    public static Filter unsafeAddFilter(String name) {
		ObjectNode searchTree = TestUtil.mapper.createObjectNode();
		searchTree.put("type", "nameeq");
		searchTree.put("name", UUID.randomUUID().toString());

        return unsafeAddFilter(name, searchTree);
	}

    public static Filter unsafeAddFilter(String name, JsonNode searchTree) {
        ObjectNode root = TestUtil.mapper.createObjectNode();
        root.put("name", name);
		root.put("searchTree", searchTree);
        

        final AtomicReference<Filter> f = new AtomicReference<>();
        TestUtil.sendRequest("/filters", root, TestUtil.Method.POST, (entity, body) -> {
            try {
                Filter filter = TestUtil.mapper.readValue(body.toString(), Filter.class);
                filter.setSearchTree(body.get("searchTree").toString());
                f.set(filter);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return  f.get();
    }

    public static void addFilterAndThenRemoveIt(Consumer<Filter> cb) {
		Filter filter = null;
        try {
            filter = unsafeAddFilter();
            cb.accept(filter);
        } finally {
            if (filter != null) {
                RemoveFilterTest.removeFilter(filter.getId());
            }
        }
    }

    @Test
    public void testAddFilter() {
        addFilterAndThenRemoveIt((filter) -> {
            assertThat(filter.getName(), is(notNullValue()));
            assertThat(filter.getSearchTree(), containsString("nameeq"));
        });
    }

    @Test
    public void testAddFilter_malformedJson() {
        StringEntity malformedJson = new StringEntity("adasd", ContentType.APPLICATION_JSON);

        HttpPost request = new HttpPost(TestUtil.ENDPOINT + "/filters");
        request.setEntity(malformedJson);
        TestUtil.sendRequest(request, (response, json) -> {
            assertThat(response.getStatusLine().getStatusCode(), is(400));
            assertThat(json.get("message").asText(), containsString("malformed json"));
        });
    }

    @Test
    public void testAddFilter_malformedSearchTree() {
        ObjectNode root = TestUtil.mapper.createObjectNode();
        root.put("name", "hej");
        root.with("searchTree").put("asdasdasd", "3");
        StringEntity malformedJson = new StringEntity(root.toString(), ContentType.APPLICATION_JSON);

        HttpPost request = new HttpPost(TestUtil.ENDPOINT + "/filters");
        request.setEntity(malformedJson);
        TestUtil.sendRequest(request, (response, json) -> {
            assertThat(response.getStatusLine().getStatusCode(), is(400));
            assertThat(json.get("message").asText(), containsString("malformed search tree"));
        });

    }

    @Test
    public void testAddFilter_duplicateName() {
        addFilterAndThenRemoveIt((firstFilter) -> {
            Filter secondFilter = unsafeAddFilter(firstFilter.getName());

            assertThat(secondFilter.getName(), is(equalTo(firstFilter.getName())));
            assertThat(secondFilter.getId(), is(not(equalTo(firstFilter.getId()))));

            RemoveFilterTest.removeFilter(secondFilter.getId());
        });
    }

    @Test
    public void addFilter_noPayload() throws IOException {
        TestUtil.sendRequest("/filters", TestUtil.Method.POST, (response, json) -> {
            assertThat(response.getStatusLine().getStatusCode(), is(400));
            assertThat(json.get("message").asText(), containsString("malformed search tree"));
        });
    }
}
