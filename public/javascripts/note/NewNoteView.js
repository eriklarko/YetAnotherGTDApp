function NewNoteView(newNoteCallback) {
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

    function clearInput() {
        newPayload.empty();
        tags.tagsinput("removeAll");
    }
}

