function ListView() {
    var notesHolder = $("<table></table>");

    this.gui = $("<div></div>");

    this.gui.append(notesHolder);


    this.showNotes = function (notes) {
        notesHolder.empty();
        $.each(notes, function(i, note) {
            console.log(note);
            var tr = $("<tr></tr>");

            var td = $("<td></td>")
            td.append(note.payload);
            tr.append(td);

            td = $("<td></td>")
            td.append(note.tags.join(";"));
            tr.append(td);

            notesHolder.append(tr);
        });
    };
}

