package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import models.Tag;
import models.TaggableObject;
import org.junit.Assert;
import org.junit.Test;
import utils.PlayIntegrationTest;

/**
 *
 * @author eriklark
 */
public class TaggableServiceTest extends PlayIntegrationTest {

	@Test
	public void testCreateTaggable() {
		String payload = "Some text";
		Collection<String> tagNames = Arrays.asList("a", "b", "c");

		Assert.assertEquals("Taggables before", 0, TaggableObject.find.all().size());
		TaggableObject actual = TaggableService.createTaggable(payload, tagNames);
		Assert.assertEquals("Taggables after", 1, TaggableObject.find.all().size());

		Assert.assertEquals(payload, actual.payload);
		Assert.assertArrayEquals(tagNames.toArray(), TagsService.getTagNames(actual.tags).toArray());
	}

	@Test(expected = NoSuchElementException.class)
	public void testRemoveTagFromUnTaggedTaggable() {
		TaggableObject taggable = new TaggableObject();
		TaggableService.removeTagFromTaggable(taggable, new Tag());
	}

	@Test
	public void testRemoveLastTagFromTaggable() {
		TaggableObject taggable = getArbitraryTaggableWithTags(Arrays.asList(UUID.randomUUID().toString()));
		TaggableService.removeTagFromTaggable(taggable, taggable.tags.get(0));

		Assert.assertEquals("Not reread", 0, taggable.tags.size());
		taggable = TaggableObject.find.byId(taggable.id);
		Assert.assertEquals("Reread", 0, taggable.tags.size());
	}

	@Test
	public void testRemoveSomeTagFromTaggable() {
		Tag tagToRemove = new Tag();
		tagToRemove.name = UUID.randomUUID().toString();
		tagToRemove.save();

		TaggableObject taggable = getArbitraryTaggableWithTags(Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString()));
		List<Tag> expected = new ArrayList<>(taggable.tags);

		taggable.tags.add(tagToRemove);
		taggable.save();

		TaggableService.removeTagFromTaggable(taggable, tagToRemove);

		Collections.sort(expected, TagsServiceTest.TAGS_SORTED_BY_NAME);
		Collections.sort(taggable.tags, TagsServiceTest.TAGS_SORTED_BY_NAME);

		Assert.assertArrayEquals("Not reread", expected.toArray(), taggable.tags.toArray());
		taggable = TaggableObject.find.byId(taggable.id);
		Collections.sort(taggable.tags, TagsServiceTest.TAGS_SORTED_BY_NAME);
		Assert.assertArrayEquals("Reread", expected.toArray(), taggable.tags.toArray());
	}

	private TaggableObject getArbitraryTaggableWithTags(List<String> tagNames) {
		TaggableObject taggable = new TaggableObject();
		taggable.payload = "asd";
		taggable.tags = TagsService.findOrCreateTags(tagNames);
		taggable.save();
		return taggable;
	}

	@Test
	public void testRemoveTaggable() {
		TaggableObject toRemove = getArbitraryTaggableWithTags(Arrays.asList(UUID.randomUUID().toString()));

		TaggableService.remove(toRemove);
		Assert.assertEquals(0, TaggableObject.all().size());
	}
}
