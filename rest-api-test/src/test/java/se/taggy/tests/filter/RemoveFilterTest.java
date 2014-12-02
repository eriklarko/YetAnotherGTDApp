package se.taggy.tests.filter;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import se.taggy.tests.util.TestBase;

import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;

public class RemoveFilterTest extends TestBase {

    public static JsonNode removeFilter(TestBase base, int filterId) {
        final AtomicReference<JsonNode> response = new AtomicReference<>();
        base.sendRequest("/filters/" + filterId, Method.DELETE, (entity, json) -> {
            response.set(json);
        });
        return response.get();
    }

    @Test
    public void testDeleteExistingFilter() {
        Filter filter = AddFilterTest.unsafeAddFilter(this);

        JsonNode json = removeFilter(this, filter.getId());

        assertThat(json.get("message").asText(), is(equalToIgnoringCase("ok")));
    }

    @Test
    public void testDeleteNonExistingFilter() {
        JsonNode json = removeFilter(this, Integer.MAX_VALUE);

        assertThat(json.get("message").asText(), is(equalToIgnoringCase("no such filter")));
    }
}
