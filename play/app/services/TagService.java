package services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import removewhenjava8.Optional;
import java.util.Set;
import javax.persistence.PersistenceException;
import models.Filter;
import models.Note;
import models.Tag;
import play.db.ebean.Transactional;
import removewhenjava8.Consumer;
import search.And;
import search.IdEq;
import search.Node;
import search.Not;
import search.Or;
import search.SearchTreeVisitor;

/**
 *
 * @author eriklark
 */
public class TagService extends BaseService<Tag> {

	private static final TagService instance = new TagService();

	public static TagService instance() {
		return instance;
	}

	private TagService() {
		super(Tag.class);
	}

	public Set<Tag> getAllCurrentUsersTags() {
		return find().findSet();
	}

	public Optional<Tag> findByName(String name) {
		try {
			Tag found = find().ieq("name", name).findUnique();
			if (found == null) {
				return Optional.empty();
			} else {
				return Optional.of(found);
			}
		} catch (PersistenceException ex) {
			System.out.println("There was a problem finding tag >" +name+ "<");
			findAndRemoveDuplicates(name);

			return findByName(name);
		}
	}

	private void findAndRemoveDuplicates(String name) {
		List<Tag> tagsWithConflictingNames = find().ieq("name", name).findList();
		Iterator<Tag> it = tagsWithConflictingNames.iterator();
		if (it.hasNext()) {
			Tag keep = it.next();

			while (it.hasNext()) {
				Tag toDelete = it.next();
				Iterable<Note> notes = NoteService.instance().findNotesWithTag(toDelete);
				for (Note note : notes) {
					System.out.println("Note " + note.id + " has the tag to remove. " + Arrays.deepToString(note.tags.toArray()));

					NoteService.instance().addTag(note, keep);
					NoteService.instance().removeTag(note, toDelete);
				}

				delete(toDelete);
			}
		}
	}

    public void delete(final Tag toDelete) {

        for (Filter f : FilterService.instance().getAllCurrentUsersFilters()) {

            SearchTreeVisitor.visit(f.getSearchTree(), new Consumer<Node>() {

                @Override
                public void accept(Node t) {
                    if(t instanceof IdEq) {
                        IdEq ideq = (IdEq) t;
                        if (ideq.getTag().equals(toDelete)) {
                            // This filter needs to be updated.
                            // If parent has other children, remove this as child
                            // If this is the parents only child, remove the parent

                            removeRecusively(ideq);
                        }
                    }
                }

                private void removeRecusively(Node n) {
                    Node parent = n.parent();

                    if (parent == null) { // We have reached the root
                        return;
                    }

                    if (parent instanceof And) {
                        And and = (And) parent;
                        and.getChildren().remove(n);

                        if (and.getChildren().isEmpty()) {
                            removeRecusively(parent);
                        }
                    } else if (parent instanceof Or) {
                        Or or = (Or) parent;
                        or.getChildren().remove(n);

                        if (or.getChildren().isEmpty()) {
                            removeRecusively(parent);
                        }
                    } else if (parent instanceof Not) {
                        removeRecusively(parent);
                    } else {
                        throw new IllegalArgumentException("Unknown node type: " + parent);
                    }
                }
            });
        }

        toDelete.delete();
    }

	public Set<String> getTagNames(Iterable<Tag> tags) {
		Set<String> tagNames = new HashSet<>();
		for (Tag tag : tags) {
			tagNames.add(tag.getName());
		}

		return tagNames;
	}

	@Transactional
	public Set<Tag> findOrCreateTags(Iterable<Tag> tagsToFindOrCreate) {
		Set<Tag> tags = new HashSet<>();
		for (Tag tag : tagsToFindOrCreate) {
			tags.add(findOrCreateTag(tag));
		}
		return tags;
	}

	private Tag findOrCreateTag(Tag tag) {
		if (tag.id == null) {
			return TagNameService.findOrCreateTagFromName(tag.getName());
		} else {
			return byId(tag.id);
		}
	}

	public boolean hasOnlyEmptyNames(Iterable<Tag> tags) {
		for (Tag tag : tags) {
			if (!(tag.getName() == null || tag.getName().trim().isEmpty())) {
				return false;
			}
		}
		return true;
	}

	@Transactional
	public Tag createNewTagFromName(String tagName) {
		Tag tag = new Tag();
		tag.owner = UserService.getCurrentUser();
		tag.setName(tagName);
		tag.save();
		return tag;
	}

	public Tag updateName(Tag tag, String newName) {
		tag.setName(newName);
		tag.save();

		return tag;
	}

	public Set<Tag> findTagsWithNameMatchingQuery(String query) {
		return find().ilike("name", query).findSet();
	}

	public Tag getArchiveTag() {
		return TagNameService.findOrCreateTagFromName("archive");
	}
}
