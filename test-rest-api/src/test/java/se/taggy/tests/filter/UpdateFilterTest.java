package se.taggy.tests.filter;

import java.util.Arrays;
import java.util.Collection;

import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

import se.taggy.tests.util.TestUtil;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class UpdateFilterTest {

    @Test
    public void updateExistingFilter() {
        AddFilterTest.addFilterAndThenRemoveIt(filter -> {

            assertThat(filter.getName(), is(not(equalTo("new name"))));
            assertThat(filter.isStarred(), is(false));

            ObjectNode filterJson = TestUtil.mapper.createObjectNode();
            filterJson.put("name", "new name");
            Collection<JsonNode> ors = Arrays.asList(TestUtil.mapper.createObjectNode().put("type", "nameeq").put("name", "1"),
                    TestUtil.mapper.createObjectNode().put("type", "nameeq").put("name", "2")
            );
            filterJson.with("searchTree").put("type", "or").withArray("children").addAll(ors);
            filterJson.put("starred", true);

            TestUtil.put("/filters/" + filter.getId(), filterJson, (response, json) -> {
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
        AddFilterTest.addFilterAndThenRemoveIt(filter -> {
            StringEntity malformedJson = new StringEntity("adasd", ContentType.APPLICATION_JSON);

            HttpPut request = new HttpPut(TestUtil.ENDPOINT + "/filters/" + filter.getId());
            request.setEntity(malformedJson);
            TestUtil.sendRequest(request, (response, json) -> {
                assertThat(response.getStatusLine().getStatusCode(), is(400));
                assertThat(json.get("message").asText(), containsString("malformed json"));
            });
        });
    }

    @Test
    public void updateExistingFilter_malformedSearchTree() {
        AddFilterTest.addFilterAndThenRemoveIt(filter -> {
            ObjectNode root = TestUtil.mapper.createObjectNode();
            root.put("name", "hej");
            root.with("searchTree").put("asdasdasd", "3");
            StringEntity malformedJson = new StringEntity(root.toString(), ContentType.APPLICATION_JSON);

            HttpPut request = new HttpPut(TestUtil.ENDPOINT + "/filters/" + filter.getId());
            request.setEntity(malformedJson);
            TestUtil.sendRequest(request, (response, json) -> {
                assertThat(response.getStatusLine().getStatusCode(), is(400));
                assertThat(json.get("message").asText(), containsString("malformed search tree"));
            });
        });
    }

    @Test
    public void updateNonExistingFilter() {
        TestUtil.put("/filters/" + Integer.MAX_VALUE, TestUtil.mapper.createObjectNode(), (response, json) -> {
            assertThat(response.getStatusLine().getStatusCode(), is(422));
            assertThat(json.get("message").asText(), containsString("no such filter"));
        });
    }
}
