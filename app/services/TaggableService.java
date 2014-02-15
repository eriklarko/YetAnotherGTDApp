package services;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import models.Tag;
import models.TaggableObject;
import play.db.ebean.Transactional;
import utils.TaggablesIdComparator;

/**
 *
 * @author eriklark
 */
public class TaggableService {

	@Transactional
	public static TaggableObject createTaggable(String payload, Collection<String> tagNames) {
		TaggableObject toCreate = new TaggableObject();
		toCreate.payload = payload;
		toCreate.tags = TagsService.findOrCreateTags(tagNames);
		toCreate.save();
		return toCreate;
	}

	/**
	 * Removes a taggable. If there are tags which are no longer connected to
	 * any taggable, they are marked for deletion.
	 */
	@Transactional
	public static void remove(TaggableObject obj) {
		List<Tag> tags = obj.tags;
		markUnusedTagsForDeletion(tags);
		obj.deleteManyToManyAssociations("tags");
		obj.delete();
	}

	private static void markUnusedTagsForDeletion(List<Tag> candidates) {
		for (Tag candidate : candidates) {
			List<TaggableObject> objectsWithTag = findTaggablesWithTag(candidate);
			if (objectsWithTag.isEmpty()) {
				candidate.delete();
			}
		}
	}

	private static List<TaggableObject> findTaggablesWithTag(Tag tag) {
		return TaggableObject.find.where().eq("tags.name", tag.name).findList();
	}

	@Transactional
	public static void removeTagFromTaggable(TaggableObject taggable, Tag tagToRemove) throws NoSuchElementException {
		boolean tagRemoved = taggable.tags.remove(tagToRemove);

		if (tagRemoved) {
			taggable.save();
		} else {
			throw new NoSuchElementException("Unable to find tag " + tagToRemove.name + " in taggable " + taggable.id);
		}
	}

	@Transactional
	public static void addTags(TaggableObject taggable, Collection<String> tagNames) {
		List<Tag> newTags = TagsService.findOrCreateTags(tagNames);
		taggable.tags.addAll(newTags);
		taggable.save();
	}

	public static Set<TaggableObject> findTaggablesTaggedWith(Collection<Tag> tags) {
		Set<TaggableObject> taggables = new TreeSet<>(new TaggablesIdComparator());
		for (Tag tag : tags) {
			taggables.addAll(findTaggablesWithTag(tag));
		}
		return taggables;
	}
}
