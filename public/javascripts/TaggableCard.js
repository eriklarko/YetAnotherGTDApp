function TaggableCard(taggable, removeTaggableListener) {
    var mainDiv = $("<div></div>");
    mainDiv.addClass("card");

    var tags = new TagRow(taggable.tags);
    var payload = new Payload(taggable.payload);

    mainDiv.append(payload);
    mainDiv.append(tags);

    mainDiv.append("<div style='clear: both'></div>");
    return mainDiv;

    function Payload(payload) {
        var removeBtn = $("<a href='#' title='Remove'>x</a>");
        removeBtn.on('click', function () {
            removeTaggableListener(taggable.id);
        });

        var div = $("<div></div>");
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