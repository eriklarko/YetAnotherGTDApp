function FilterView(options) {
    var self = this;
    var defaultOptions = {
        appendTo: $("<div></div>")
    };
    self.options = $.extend(defaultOptions, options);

    self.setFilter = function (filter) {
        var cards = buildCards();
        var notesHolder = $("<div></div>");
        cards.append(notesHolder);
        cards.append(new NewNoteView(function(newNote) {
            notesHolder.append(buildNote(newNote));
        }));

        self.options.appendTo.empty();
        self.options.appendTo.append(buildHeader());

        self.options.appendTo.append(cards);

        loadObjectsInFilter(filter.id, function(notes) {
            showNotes(notes, notesHolder);
        });

        function buildHeader() {
            var wrapper = $("<div></div>");

            var header = $("<h2></h2>");
            header.append("Filter " + filter.name);
            wrapper.append(header);

            wrapper.append("shows tags matching");
            wrapper.append(new FilterSearchTreeView(filter, function (updatedFilter) {
                loadObjectsInFilter(updatedFilter.id, function (notes) {
                    showNotes(notes, notesHolder);
                });
            }));


            return wrapper;
        }

        function buildCards() {
            var cards = $("<div class='cards'></div>");
            return cards;
        }

        function showNotes(notes, appendTo) {
            appendTo.empty();
            $.each(notes, function(i, note) {
                appendTo.append(buildNote(note));
            });
        }

        function buildNote(note) {
            var card = new NoteCard(note, function (noteId) {
                addTagToNote(noteId, "archive", function(updatedNote) {
                    tagsRow.tagsinput("removeAll");
                    tagsRow.tagsinput("addAll", updatedNote.tags);
                });
            });
            card.attr("id", "note-" + note.id);

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
    };
}