function FiltersView(options) {
    var self = this;

    var defaultOptions = {
        filters: "all",
        showAddFilter: true,

        appendTo: $("<div></div>"),
        onFilterChanged: function (newFilter) {}
    };

    self.options = $.extend(defaultOptions, options);

    self.populate = function () {
        if (self.options.filters === "all") {
            getAllFilters(function (filters) {
                self.options.filters = filters;
                render();
            });
        } else {
            render();
        }
    }

    function render() {
        var tr = $("<tr></tr>");

        renderFilters(tr);

        var td = $("<td></td>");
        td.append(buildNewFilterView());
        tr.append(td);

        var table = $("<table></table>");
        table.append(tr);

        self.options.appendTo.empty();
        self.options.appendTo.append(table);
    }

    function renderFilters(appendTo) {
        $.each(self.options.filters, function(i, filter) {
            var view = createFilterView(filter);

            var td = $("<td></td>");
            td.append(view);
            appendTo.append(td);
        });
    }

    function createFilterView(filter) {
        var link = $("<a href='#'>" + filter.name + "</a>");
        link.addClass("filter");
        link.on("click", function () {
           self.options.onFilterChanged(filter);
        });
        return link;
    }

    function buildNewFilterView() {
        var name = $("<input type='text' placeholder='Filter name' />");
        var tags = $("<select multiple placeholder='Tags'></select>");
        var addBtn = $("<button>Create</button>");
        addBtn.css("float", "right");
        addBtn.on("click", function() {
            addFilter(name.val(), getSearchTree(), function (newFilter) {
                clearInput();
                self.options.appendTo.append(createFilterView(newFilter));
            });
        });

        var wrapper = $("<fieldset></fieldset>");
        wrapper.css("width", "17em");
        wrapper.append("<legend>New filter</legend>");
        wrapper.append(name);
        wrapper.append(tags);
        wrapper.append(addBtn);

        setupTagsBox(tags);

        return wrapper;

        function getSearchTree() {
            var tagsOr = {
                type: "or",
                children: []
            };
            $.each(getTags(), function (i, tag) {
               tagsOr.children.push({
                  type: "ideq",
                  id: tag.id,
                  name: tag.name
               });
            });

            return {
                type: "and",
                children: [{
                    type: "not",
                    child: {
                        type: "ideq",
                        name: "archive"
                    }
                }, tagsOr]
            };
        }

        function getTags() {
            var tagNames = [];
            $.each(tags.tagsinput("items"), function (i, tagWithIffyId) {
                if (!isNumber(tagWithIffyId.id)) {
                    var newTag = {
                        name: tagWithIffyId.name
                    };
                    tagNames.push(newTag);
                } else {
                    tagNames.push(tagWithIffyId);
                }
            });
            return tagNames;
        }

        function clearInput() {
            name.val("");
            tags.tagsinput('removeAll');
        }
    }
}
