function FilterView(options) {
    var self = this;
    var defaultOptions = {
        appendTo: $("<div></div>"),
        view: new CardView()
    };
    self.options = $.extend(defaultOptions, options);

    self.setFilter = function (filter) {
        self.options.appendTo.empty();
        self.options.appendTo.append(buildHeader());

        self.options.appendTo.append(self.options.view.gui);

        loadObjectsInFilter(filter.id, self.options.view.showNotes);

        function buildHeader() {
            var wrapper = $("<div></div>");

            var header = $("<h2></h2>");
            header.append("Filter " + filter.name);
            wrapper.append(header);

            wrapper.append("shows tags matching");
            wrapper.append(new FilterSearchTreeView(filter, function (updatedFilter) {
                loadObjectsInFilter(updatedFilter.id, self.options.view.showNotes);
            }));

            return wrapper;
        }
    };
}