package se.taggy.tests.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.junit.Test;
import se.taggy.tests.util.TestBase;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class UpdateFilterTest extends TestBase {

    @Test
    public void updateExistingFilter() {
        AddFilterTest.addFilterAndThenRemoveIt(this, filter -> {

            assertThat(filter.getName(), is(not(equalTo("new name"))));
            assertThat(filter.isStarred(), is(false));

            ObjectNode filterJson = mapper.createObjectNode();
            filterJson.put("name", "new name");
            Collection<JsonNode> ors = Arrays.asList(
                    mapper.createObjectNode().put("type", "ideq").put("id", "1"),
                    mapper.createObjectNode().put("type", "ideq").put("id", "2")
            );
            filterJson.with("searchTree").put("type", "or").withArray("children").addAll(ors);
            filterJson.put("starred", true);

            put("/filters/" + filter.getId(), filterJson, (response, json) -> {
                assertThat(json.get("id").asInt(), is(filter.getId()));
                assertThat(json.get("name").asText(), is("new name"));
                assertThat(json.get("starred").asBoolean(), is(true));

                assertThat(json.get("searchTree").get("type").asText(), is("or"));
                assertThat("Expected array", json.get("searchTree").get("children").isArray());
                assertThat(json.get("searchTree").get("children").size(), is(2));
            });
        });
    }

    @Test
    public void updateExistingFilter_malformedJson() {
        AddFilterTest.addFilterAndThenRemoveIt(this, filter -> {
            StringEntity malformedJson = new StringEntity("adasd", ContentType.APPLICATION_JSON);

            HttpPut request = new HttpPut(ENDPOINT + "/filters/" + filter.getId());
            request.setEntity(malformedJson);
            sendRequest(request, (response, json) -> {
                assertThat(response.getStatusLine().getStatusCode(), is(400));
                assertThat(json.get("message").asText(), containsString("malformed json"));
            });
        });
    }

    @Test
    public void updateExistingFilter_malformedSearchTree() {
        AddFilterTest.addFilterAndThenRemoveIt(this, filter -> {
            ObjectNode root = mapper.createObjectNode();
            root.put("name", "hej");
            root.with("searchTree").put("asdasdasd", "3");
            StringEntity malformedJson = new StringEntity(root.toString(), ContentType.APPLICATION_JSON);

            HttpPut request = new HttpPut(ENDPOINT + "/filters/" + filter.getId());
            request.setEntity(malformedJson);
            sendRequest(request, (response, json) -> {
                assertThat(response.getStatusLine().getStatusCode(), is(400));
                assertThat(json.get("message").asText(), containsString("malformed search tree"));
            });
        });
    }

    @Test
    public void updateNonExistingFilter() {
        put("/filters/" + Integer.MAX_VALUE, mapper.createObjectNode(), (response, json) -> {
            assertThat(response.getStatusLine().getStatusCode(), is(400));
            assertThat(json.get("message").asText(), containsString("no such filter"));
        });
    }
}
