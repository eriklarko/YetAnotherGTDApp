package se.taggy.tests.filter;

import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import org.junit.Assert;
import org.junit.Test;

import se.taggy.tests.util.TOrJson;
import se.taggy.tests.util.TestUtil;

import com.fasterxml.jackson.databind.JsonNode;

public class RemoveFilterTest {

    public static JsonNode removeFilter(int filterId) {
        final AtomicReference<JsonNode> response = new AtomicReference<>();
        TestUtil.sendRequest("/filters/" + filterId, TestUtil.Method.DELETE, (entity, json) -> {
            response.set(json);
        });
        return response.get();
    }

    @Test
    public void testDeleteExistingFilter() {
        Filter filter = AddFilterTest.unsafeAddFilter();

        JsonNode json = removeFilter(filter.getId());
        assertThat(json.get("message").asText(), is(equalToIgnoringCase("ok")));

		TOrJson<Filter> loadedFilter = ListFiltersTest.loadFilter(filter.getId());
		Assert.assertFalse(loadedFilter.hasT());
    }

    @Test
    public void testDeleteNonExistingFilter() {
        JsonNode json = removeFilter(Integer.MAX_VALUE);

        assertThat(json.get("message").asText(), is(equalToIgnoringCase("no such filter")));
    }
}
