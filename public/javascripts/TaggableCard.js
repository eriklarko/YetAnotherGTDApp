function TaggableCard(taggable, removeTaggableListener, removeTagListener) {
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

        var row = $("<div></div>");
        $.each(tags, function(i, tag) {
           row.append(new Tag(tag));
        });

        return row;

        function Tag(tag) {
            var removeBtn = $("<a href='#' title='Remove'>x</a>");
            removeBtn.on('click', function () {
                removeTagListener(taggable.id, tag.id);
            });

            var div = $("<div></div>");
            div.addClass("tag");
            div.attr("id", "tag-" + tag.id);
            div.append(tag.name + "&nbsp;&nbsp;");
            div.append(removeBtn);

            return div;
        }
    }
}