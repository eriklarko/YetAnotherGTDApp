package se.taggy.tests.filter;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import se.taggy.tests.util.TestBase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;

public class ListFiltersTest extends TestBase {

    @Test
    public void testListAllFilters() {
        Filter filter1 = AddFilterTest.unsafeAddFilter(this);
        Filter filter2 = AddFilterTest.unsafeAddFilter(this);
        Filter filter3 = AddFilterTest.unsafeAddFilter(this);

        sendRequest("/filters", Method.GET, (entity, body) -> {

            assertThat("Expected array", body.isArray());
            assertThat("Expected non-empty array", body.size(), is(greaterThan(0)));
            for (JsonNode node : body) {
                assertThat("Expected non-null id", node.hasNonNull("id"));
                assertThat("Expected non-null name", node.hasNonNull("name"));
                assertThat("Expected non-null searchTree", node.hasNonNull("searchTree"));
                assertThat("Expected non-null starred", node.hasNonNull("starred"));
            }
        });

        RemoveFilterTest.removeFilter(this, filter1.getId());
        RemoveFilterTest.removeFilter(this, filter2.getId());
        RemoveFilterTest.removeFilter(this, filter3.getId());
    }

    @Test
    public void testGetSpecificFilter() {
        AddFilterTest.addFilterAndThenRemoveIt(this, (addedFilter) -> {

            sendRequest("/filters/" + addedFilter.getId(), Method.GET, (entity, body) -> {
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
