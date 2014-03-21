function CardView() {
    var notesHolder = $("<div></div>");

    this.gui = $("<div class='cards'></div>");

    this.gui.append(notesHolder);
    /*this.gui.append(new NewNoteView(function(newNote) {
        notesHolder.append(buildCard(newNote));
    }));*/

    this.showNotes = function (notes) {
        notesHolder.empty();
        $.each(notes, function(i, note) {
            notesHolder.append(buildCard(note));
        });
    };

    function buildCard(note) {
        var card = new NoteCard(note, function (noteId) {
            addTagToNote(noteId, "archive", function(updatedNote) {
                tagsRow.tagsinput("removeAll");
                tagsRow.tagsinput("addAll", updatedNote.tags);
            });
        });
        card.attr("id", "note-" + note.id);
        card.css("width", "15em");
        card.css("float", "left");
        card.css("margin", "0.5em");

        var tagsRow = card.children(".tagsRow");
        setupTagsBox(tagsRow, {
            onAddTag: function (tag) {
                addTagToNote(note.id, tag.name, function (updatedNote) {
                    $.each(updatedNote.tags, function(i, newTag) {
                        if (newTag.name === tag.name) {
                            tag.id = newTag.id;
                        }
                     });
                });
            },
            onRemoveTag: function (tag) {
                removeTagFromNote(note.id, tag.id);
            }
        });
        tagsRow.tagsinput("addAll", note.tags);

        return card;
    }

}