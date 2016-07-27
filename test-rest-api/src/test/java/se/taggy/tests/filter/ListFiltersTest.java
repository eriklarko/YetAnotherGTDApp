package se.taggy.tests.filter;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;
import org.junit.Test;

import se.taggy.tests.util.TOrJson;
import se.taggy.tests.util.TestUtil;

import com.fasterxml.jackson.databind.JsonNode;

public class ListFiltersTest {

	public static TOrJson<Filter> loadFilter(int filterId) {
		final AtomicReference<TOrJson> note = new AtomicReference<>();
        TestUtil.sendRequest("/filters/" + filterId, TestUtil.Method.GET, (entity, responseAsJson) -> {
			try {
				Filter f = TestUtil.mapper.readValue(responseAsJson.toString(), Filter.class);
				note.set(new TOrJson<>(f));
			} catch (IOException ex) {
				note.set(new TOrJson<>(responseAsJson));
			}
        });

		return note.get();
    }

    @Test
    public void testListAllFilters() {
        Filter filter1 = AddFilterTest.unsafeAddFilter();
        Filter filter2 = AddFilterTest.unsafeAddFilter();
        Filter filter3 = AddFilterTest.unsafeAddFilter();

        TestUtil.sendRequest("/filters", TestUtil.Method.GET, (entity, body) -> {

            assertThat("Expected array", body.isArray());
            assertThat("Expected non-empty array", body.size(), is(greaterThan(0)));
            for (JsonNode node : body) {
                assertThat("Expected non-null id", node.hasNonNull("id"));
                assertThat("Expected non-null name", node.hasNonNull("name"));
                assertThat("Expected non-null searchTree", node.hasNonNull("searchTree"));
                assertThat("Expected non-null starred", node.hasNonNull("starred"));
            }
        });

        RemoveFilterTest.removeFilter(filter1.getId());
        RemoveFilterTest.removeFilter(filter2.getId());
        RemoveFilterTest.removeFilter(filter3.getId());
    }

    @Test
    public void testGetSpecificFilter() {
        AddFilterTest.addFilterAndThenRemoveIt((addedFilter) -> {
			int filterId = addedFilter.getId();

            TestUtil.sendRequest("/filters/" + filterId, TestUtil.Method.GET, (entity, body) -> {
                System.out.println(body);
                assertThat("Expected object", body.isObject());
                assertThat("Expected non-empty object", body.size(), is(4));
                assertThat("id differed", body.get("id").asText(), is(equalTo(addedFilter.getId())));
                assertThat("Name differed", body.get("name").asText(), is(equalTo(addedFilter.getName())));
                assertThat("Search tree differed", body.get("searchTree").asText(), is(equalTo(addedFilter.getSearchTree())));
                assertThat("Starred differed", body.get("starred").asText(), is(equalTo(addedFilter.isStarred())));
            });
        });
    }
}
