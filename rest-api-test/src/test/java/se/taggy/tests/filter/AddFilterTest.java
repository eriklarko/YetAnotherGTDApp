package se.taggy.tests.filter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.junit.Assert;
import org.junit.Test;
import se.taggy.tests.util.TestBase;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class AddFilterTest extends TestBase {

    /**
     * Does not clean up after itself. Use with caution!
     */
    public static Filter unsafeAddFilter(TestBase base) {
        return unsafeAddFilter(base, UUID.randomUUID().toString());
    }

    public static Filter unsafeAddFilter(TestBase base, String name) {
        ObjectNode root = mapper.createObjectNode();
        root.put("name", name);
        root.with("searchTree")
                .put("type", "ideq")
                .put("id", "261"); // TODO: Create random tag

        final AtomicReference<Filter> f = new AtomicReference<>();
        base.sendRequest("/filters", root, Method.POST, (entity, body) -> {
            try {
                Filter filter = mapper.readValue(body.toString(), Filter.class);
                filter.setSearchTree(body.get("searchTree").toString());
                f.set(filter);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return  f.get();

    }

    public static void addFilterAndThenRemoveIt(TestBase base, Consumer<Filter> cb) {
        Filter filter = null;
        try {
            filter = unsafeAddFilter(base);
            cb.accept(filter);
        } finally {
            if (filter != null) {
                RemoveFilterTest.removeFilter(base, filter.getId());
            }
        }
    }

    @Test
    public void testAddFilter() {
        addFilterAndThenRemoveIt(this, (filter) -> {
            assertThat(filter.getName(), is(notNullValue()));
            assertThat(filter.getSearchTree(), containsString("ideq"));
            assertThat(filter.getSearchTree(), containsString("261"));
        });
    }

    @Test
    public void testAddFilter_malformedJson() {
        StringEntity malformedJson = new StringEntity("adasd", ContentType.APPLICATION_JSON);

        HttpPost request = new HttpPost(ENDPOINT + "/filters");
        request.setEntity(malformedJson);
        sendRequest(request, (response, json) -> {
            assertThat(response.getStatusLine().getStatusCode(), is(400));
            assertThat(json.get("message").asText(), containsString("malformed json"));
        });
    }

    @Test
    public void testAddFilter_malformedSearchTree() {
        ObjectNode root = mapper.createObjectNode();
        root.put("name", "hej");
        root.with("searchTree").put("asdasdasd", "3");
        StringEntity malformedJson = new StringEntity(root.toString(), ContentType.APPLICATION_JSON);

        HttpPost request = new HttpPost(ENDPOINT + "/filters");
        request.setEntity(malformedJson);
        sendRequest(request, (response, json) -> {
            assertThat(response.getStatusLine().getStatusCode(), is(400));
            assertThat(json.get("message").asText(), containsString("malformed search tree"));
        });

    }

    @Test
    public void testAddFilter_duplicateName() {
        addFilterAndThenRemoveIt(this, (firstFilter) -> {
            Filter secondFilter = unsafeAddFilter(this, firstFilter.getName());

            assertThat(secondFilter.getName(), is(equalTo(firstFilter.getName())));
            assertThat(secondFilter.getId(), is(not(equalTo(firstFilter.getId()))));

            RemoveFilterTest.removeFilter(this, secondFilter.getId());
        });
    }

    @Test
    public void addFilter_noPayload() throws IOException {
        sendRequest("/filters", Method.POST, (response, json) -> {
            assertThat(response.getStatusLine().getStatusCode(), is(400));
            assertThat(json.get("message").asText(), containsString("malformed search tree"));
        });
    }
}
