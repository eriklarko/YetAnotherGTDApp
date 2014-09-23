function TagList(tags) {
    tags.sort(function (a, b) {
       return stringComp(a.name, b.name);
    });

    var tagList = $("<div></div>");
    $.each(tags, function(i, tag) {
        var wrapper = $("<div></div>");

        var tagName = $("<a href='#'></a>");
        tagName.append(tag.name);
        tagName.on("click", function () {
            moveTo("/tag/" + tag.id);
        });

        wrapper.append(tagName);
        wrapper.append(" (" + tag.numNotes + ")");

        tagList.append(wrapper);
    });

    return tagList;
}