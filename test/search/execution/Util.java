package search.execution;

import com.google.common.collect.Sets;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import models.Note;
import models.Tag;
import services.NoteService;
import services.TagNameService;

/**
 *
 * @author Thinner
 */
public class Util {

    private static final Comparator<Note> NOTE_ID_COMPARATOR = new Comparator<Note>() {

        @Override
        public int compare(Note o1, Note o2) {
            return o1.id.compareTo(o2.id);
        }
    };

    public static SortedSet<Note> sort(Note... toSort) {
        return sort(Sets.newHashSet(toSort));
    }

    public static SortedSet<Note> sort(Set<Note> toSort) {
        return sort(toSort, NOTE_ID_COMPARATOR);
    }

    public static <T> SortedSet<T> sort(Set<T> toSort, Comparator<? super T> comparator) {
        SortedSet<T> sorted = new TreeSet<>(comparator);
        sorted.addAll(toSort);
        return sorted;
    }

    public static Note createNote(Tag... tags) {
         return NoteService.create(UUID.randomUUID().toString(), Sets.newHashSet(tags));
    }

    public static SortedSet<Note> createNotes(int count, Tag... tags) {
        SortedSet<Note> notes = new TreeSet<>(NOTE_ID_COMPARATOR);
        for (int i = 0; i < count; i++) {
            notes.add(createNote(tags));
        }
        return notes;
    }

    public static Tag createTag() {
        return TagNameService.findOrCreateTagFromName(UUID.randomUUID().toString());
    }

    private Util() {
    }
}
