function SearchView(query, matches) {

    var tags = $("<div></div>");
    tags.css("float", "left");
    tags.append("<h3>" + matches.tags.length + " " + singleWordPlural(matches.tags.length, "tag", "tags") + " matched your query</h3>");
    $.each(matches.tags, function(i, tag) {
        var tagName = $("<a href='/tag/" +tag.id+ "'></a>");
        tagName.append(tag.name);

        tags.append(tagName);
        tags.append("<br/>");
    });

    var payloads = $("<div></div>");
    payloads.css("float", "left");
    payloads.css("margin-left", "1em");
    payloads.append("<h3>" + matches.notes.length + " " + singleWordPlural(matches.notes.length, "note", "notes") + " matched your query</h3>");
    var cv = new CardView();
    cv.showNotes(matches.notes);
    payloads.append(cv.gui);

    var div = $("<div></div>");
    div.append(tags);
    div.append(payloads);

    return div;

    function singleWordPlural(num, singular, plural) {
        if (num === 1) {
            return singular;
        } else {
            return plural;
        }
    }
}