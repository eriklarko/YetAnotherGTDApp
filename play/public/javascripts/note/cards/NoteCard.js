function NoteCard(note, removeNoteListener) {
    var mainDiv = $("<div></div>");
    mainDiv.addClass("card");

    var tags = $("<div></div>");
    tags.addClass("tagsRow");
    var payload = buildPayload(note.payload);

    mainDiv.append(payload);
    mainDiv.append(tags);

    mainDiv.append("<div style='clear: both; height: 0.5em'></div>");
    return mainDiv;

    function buildPayload(payload) {
        var removeBtn = $("<a href='#' title='Archive'>&times;</a>");
        removeBtn.css("float", "right");
        removeBtn.addClass("remove");
        removeBtn.on('click', function () {
            removeNoteListener(note.id);
        });

        var payloadWidget = new NewPayload({
            payload: payload,
            defaultMode: "uneditable",
            onSubmit: function (value) {
                updatePayload(note.id, value, undefined, function () {
                    payloadWidget.setValue(payload);
                    alert("ERROROROROOR :)");
                });
            }
        });

        var wrapper = $("<div></div>");
        wrapper.append(removeBtn);
        wrapper.append(payloadWidget.ui);

        return wrapper;
    }
}