package se.taggy.tests.note;

import se.taggy.tests.tag.Tag;

import java.util.List;

public class Note {

    private int id;
    private String payload;
    private List<Tag> tags;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;
        return id == note.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
