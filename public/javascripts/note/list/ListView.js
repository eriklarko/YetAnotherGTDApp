function ListView() {
    var thead = $("<thead></thead>");
    var tr = $("<tr></tr>");
    tr.append("<th>Payload</th>");
    tr.append("<th>Tags</th>");
    thead.append(tr);

    var tbody = $("<tbody></tbody>");
    var notesHolder = $("<table></table>");
    notesHolder.append(thead);
    notesHolder.append(tbody);

    this.gui = $("<div></div>");

    this.gui.append(notesHolder);
    //  this.gui.append(new NewNoteView());

    this.showNotes = function (notes) {
        tbody.empty();

        $.each(notes, function(i, note) {
            var payload = new NewPayload(note.payload);
            var tags = new TagsInput();
            tags.addAll(note.tags);

            var tr = $("<tr></tr>");
            var td = $("<td></td>");
            td.append(payload);
            tr.append(td);

            td = $("<td></td>");
            td.append(tags.wrapper);
            tr.append(td);

            tbody.append(tr);
            console.log(tbody);
        });
    };
}

