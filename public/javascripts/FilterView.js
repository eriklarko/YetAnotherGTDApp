function FilterView(options) {
    var self = this;
    var defaultOptions = {
        appendTo: $("<div></div>")
    };
    self.options = $.extend(defaultOptions, options);

    self.setFilter = function (filter) {
        self.options.appendTo.empty();
        self.options.appendTo.append(buildHeader());
        self.options.appendTo.append("shows tags matching")
        self.options.appendTo.append(buildSearchTree());

        var cards = buildCards();
        self.options.appendTo.append(cards);
        self.options.appendTo.append(buildNewNote(function(newNote) {
            cards.append(buildNote(newNote));
        }));

        loadObjectsInFilter(filter.id, function(notes) {
            showNotes(notes, cards);
        });

        function buildHeader() {
            var header = $("<h2></h2>");
            header.append("Filter " + filter.name);
            return header;
        }

        function buildSearchTree () {
            var tagsInput = $("<select multiple placeholder='Tags'></select>");
            var tags = findTagsInSearchTree(filter.searchTree);

            var searchTree = $("<div></div>");
            searchTree.css("width", "17em");
            searchTree.append(tagsInput);

            setupTagsBox(tagsInput);
            tagsInput.tagsinput("addAll", tags);
            return searchTree;

            function findTagsInSearchTree(searchTree, foundTags) {
                if (foundTags === undefined) {
                    var foundTags = [];
                }

                if (searchTree.type === "ideq") {
                    if (searchTree.name !== "archive") {
                        foundTags.push({
                            id: searchTree.id,
                            name: searchTree.name
                        });
                    }
                } else if (searchTree.child !== undefined) {
                    return findTagsInSearchTree(searchTree.child, foundTags);
                } else if (searchTree.children !== undefined) {
                    $.each(searchTree.children, function(i, child) {
                        findTagsInSearchTree(child, foundTags);
                    });
                }

                return foundTags;
            }

            function getSearchTree(tags) {
                var tagsOr = {
                    type: "or",
                    children: []
                };
                $.each(tags, function (i, tag) {
                   tagsOr.children.push({
                      type: "ideq",
                      id: tag.id,
                      name: tag.name
                   });
                });

                return {
                    type: "and",
                    children: [{
                        type: "not",
                        child: {
                            type: "ideq",
                            name: "archive"
                        }
                    }, tagsOr]
                };
            }
        }

        function buildCards() {
            var cards = $("<div></div>");
            return cards;
        }

        function buildNewNote(newNoteCallback) {
            var newPayload = new Payload();
            var addBtn = $("<button>Add</button>");
            addBtn.css("float", "right");
            addBtn.on("click", function () {
                var payload = newPayload.val();
                var tags = getNewTags();

                addNote(payload, tags, function (newNote) {
                    clearInput();
                    newNoteCallback(newNote);
                });
            });

            var tags = $("<select multiple placeholder='Tags'></select>");

            var wrapper = $("<div></div>");
            wrapper.css("width", "17em");
            wrapper.addClass("card");
            wrapper.append("<h2>New Note</h2>");

            wrapper.append(newPayload.ui);
            wrapper.append("<div class='line-separator'></div>");
            wrapper.append(tags);
            wrapper.append("<div class='line-separator'></div>");
            wrapper.append(addBtn);

            setupTagsBox(tags);
            return wrapper;

            function getNewTags() {
                return getTagsFromTagsInput(tags);
            }

            function clearInput() {
                newPayload.empty();
                tags.tagsinput("removeAll");
            }
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

        function getTagsFromTagsInput(input) {
            var tags = [];
            $.each(input.tagsinput("items"), function (i, tagWithIffyId) {
                if (!isNumber(tagWithIffyId.id)) {
                    var newTag = {
                        name: tagWithIffyId.name
                    };
                    tags.push(newTag);
                } else {
                    tags.push(tagWithIffyId);
                }
            });
            return tags;
        }
    };
}