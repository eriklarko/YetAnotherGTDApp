function NoteCard(note, removeNoteListener) {
    var mainDiv = $("<div></div>");
    mainDiv.addClass("card");

    var tags = $("<div></div>");
    tags.addClass("tagsRow");
    var payload = new Payload(note.payload);

    mainDiv.append(payload);
    mainDiv.append(tags);

    mainDiv.append("<div style='clear: both'></div>");
    return mainDiv;

    function Payload(payload) {
        var removeBtn = $("<a href='#' title='Archive'>x</a>");
        removeBtn.css("float", "right");
        removeBtn.addClass("remove");
        removeBtn.on('click', function () {
            removeNoteListener(note.id);
        });

        var payloadAsText = $("<div></div>");
        payloadAsText.addClass("payload");
        payloadAsText.addClass("text");
        payloadAsText.append(payload);
        payloadAsText.editable({
            type: 'text',
            showbuttons: false,
            mode: "inline",

            url: function (params) {
                var deferred = new $.Deferred;
                updatePayload(note.id, params.value,
                    function () {
                        deferred.resolve();
                    }, function (xhr) {
                        deferred.reject(xhr);
                });

                return deferred.promise();
            }
        });

        var wrapper = $("<div></div>");
        wrapper.append(removeBtn);
        wrapper.append(payloadAsText);

        return wrapper;
    }
}