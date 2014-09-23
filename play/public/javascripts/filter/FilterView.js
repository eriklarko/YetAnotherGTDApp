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

            var searchTreeView = new FilterSearchTreeView({
                onAddTag: function (tag) {
                    updateFilter(filter.id, filter.name, searchTreeView.getSearchTree(), function (updatedFilter) {
                        var tags = searchTreeView.findTagsInSearchTree(updatedFilter.searchTree);
                        $.each(tags, function(i, newTag) {
                          if (newTag.name === tag.name) {
                              tag.id = newTag.id;
                          }
                        });
                        loadObjectsInFilter(updatedFilter.id, options.view.showNotes);
                    });
                },
                onRemoveTag: function (tag) {
                    updateFilter(filter.id, filter.name, searchTreeView.getSearchTree(), function (updatedFilter) {
                        loadObjectsInFilter(updatedFilter.id, options.view.showNotes);
                    });
                },
                searchTree: filter.searchTree
            });
            wrapper.append("shows tags matching");
            wrapper.append(searchTreeView.gui);

            return wrapper;
        }
    };
}