function FiltersView(options) {
    var self = this;

    var defaultOptions = {
        filters: "all",
        showAddFilter: true,

        appendTo: $("<div></div>"),
        onFilterChanged: function (newFilter) {}
    };
    self.options = $.extend(defaultOptions, options);
    self.tr = $("<tr></tr>");
    self.populate = function () {
        if (self.options.filters === "all") {
            getAllFilters(function (filters) {
                self.options.filters = filters;
                render();
            });
        } else {
            render();
        }
    };

    function render() {
        var td = $("<td></td>");
        td.append(buildAddFilterPopup());
        td.css("width", "3em");
        self.tr.append(td);

        renderFilters(self.tr);

        var table = $("<table></table>");
        table.append(self.tr);

        self.options.appendTo.empty();
        self.options.appendTo.append(table);
    }

    function renderFilters(appendTo) {
        $.each(self.options.filters, function(i, filter) {
            var view = buildFilterView(filter);

            var td = $("<td></td>");
            td.append(view);
            appendTo.append(td);
        });
    }

    function buildAddFilterPopup() {
        var btn = $("<button>+</button>");
        var newFilterView = new NewFilterView({
            onCreate: function (newFilter) {
                var view = buildFilterView(newFilter);

                var td = $("<td></td>");
                td.append(view);
                self.tr.append(td);

                popup.toggle();
            }
        });
        var popup = new Popup(newFilterView, btn);

        return btn;
    }

    function buildFilterView(filter) {
        var link = $("<a href='#'>" + filter.name + "</a>");
        link.addClass("filter");
        link.on("click", function () {
           self.options.onFilterChanged(filter);
        });
        return link;
    }
}
