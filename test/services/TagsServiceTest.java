package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import models.Tag;
import org.junit.Assert;
import org.junit.Test;
import utils.PlayIntegrationTest;

/**
 *
 * @author eriklark
 */
public class TagsServiceTest extends PlayIntegrationTest {

	public final static Comparator<Tag> TAGS_SORTED_BY_NAME = new Comparator<Tag>() {

		@Override
		public int compare(Tag o1, Tag o2) {
			return o1.name.compareTo(o2.name);
		}
	};

	@Test
	public void testFindOrCreateOnlyCreate() {
		Assert.assertEquals("Tags present before test", 0, Tag.all().size());

		List<String> newTagNames = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());
		List<Tag> tags = TagsService.findOrCreateTags(newTagNames);
		List<String> createdTagNames = TagsService.getTagNames(tags);

		Collections.sort(newTagNames);
		Collections.sort(createdTagNames);

		Assert.assertArrayEquals(newTagNames.toArray(), createdTagNames.toArray());
	}

	@Test
	public void testFindOrCreateOnlyFind() {
		Assert.assertEquals("Tags present before test", 0, Tag.all().size());

		List<String> tagNames = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());

		List<Tag> tagsCreated = buildTagsFromNames(tagNames);
		for (Tag t : tagsCreated) {
			t.save();
		}
		List<Tag> tagsFound = TagsService.findOrCreateTags(tagNames);

		Assert.assertEquals("Too many tags created", 2, Tag.all().size());

		Collections.sort(tagsCreated, TAGS_SORTED_BY_NAME);
		Collections.sort(tagsFound, TAGS_SORTED_BY_NAME);

		Assert.assertArrayEquals(tagsCreated.toArray(), tagsFound.toArray());
	}

	@Test
	public void testGetTagNames() {
		List<String> expecteds = Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
		List<Tag> tags = buildTagsFromNames(expecteds);
		List<String> actuals = TagsService.getTagNames(tags);

		Collections.sort(expecteds);
		Collections.sort(actuals);

		Assert.assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	private List<Tag> buildTagsFromNames(Iterable<String> names) {
		List<Tag> tags = new ArrayList<>();
		for (String name : names) {
			Tag t = new Tag();
			t.name = name;
			tags.add(t);
		}
		return tags;
	}
}
