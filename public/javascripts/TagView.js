function TagView(tag, notes, options) {
    var defaultOptions = {
        notesView: new CardView()
    };
    options = $.extends(defaultOptions, options);

    var header = $("<h3></h3>");
    header.append("Showing tag " + tag.getName());

    var notesWrapper = $("<div></div>");
    options.notesView.showNotes(notes);
    notesWrapper.append(options.notesView.gui);

    var wrapper = $("<div></div>");
    wrapper.append(header);
    wrapper.append(notesWrapper);

    return wrapper;
}
