function FilterView(options) {
    var self = this;
    var defaultOptions = {
        appendTo: $("<div></div>"),
        view: new CardView()
    };
    options = $.extend(defaultOptions, options);

    self.setFilter = function (filter) {
        options.appendTo.empty();
        options.appendTo.append(buildHeader());
        options.appendTo.append("<br/>");

        options.appendTo.append(options.view.gui);

        loadObjectsInFilter(filter.id, options.view.showNotes);

        function buildHeader() {
            var wrapper = $("<div></div>");

            var header = $("<h2></h2>");
            header.append("Filter " + filter.name);
            wrapper.append(header);

            wrapper.append("shows tags matching");
            wrapper.append(new FilterSearchTreeView(filter, function (updatedFilter) {
                loadObjectsInFilter(updatedFilter.id, options.view.showNotes);
            }));

            return wrapper;
        }
    };
}