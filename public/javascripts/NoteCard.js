function NoteCard(note, removeNoteListener) {
    var mainDiv = $("<div></div>");
    mainDiv.addClass("card");

    var tags = new TagRow(note.tags);
    var payload = new Payload(note.payload);

    mainDiv.append(payload);
    mainDiv.append(tags);

    mainDiv.append("<div style='clear: both'></div>");
    return mainDiv;

    function Payload(payload) {
        var removeBtn = $("<a href='#' title='Remove'>x</a>");
        removeBtn.css("float", "right");
        removeBtn.addClass("remove");
        removeBtn.on('click', function () {
            removeNoteListener(note.id);
        });

        var div = $("<div></div>");
        div.addClass("payload");
        div.addClass("text");
        div.append(payload);
        div.append(removeBtn);

        return div;
    }

    function TagRow(tags) {
        var tagNames = new Array();
        $.each(tags, function (i, tag) {
            tagNames.push(tag.name);
        });
        var input = $("<input type='text' />");
        input.attr("tag-data", tagNames.join(","));
        return input;
    }
}