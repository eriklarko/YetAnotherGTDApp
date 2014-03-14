function TagsInput (options) {

    var defaultOptions = {
        buildTag: function (tag) {
            var div = $("<div></div>");
            div.addClass("tag");
            div.addClass("label");
            div.addClass("label-info");

            div.append(tag.name);

            return div;
        },
        idValue: function (tag) {
            return tag.name;
        },
        onAdd: function (tag) {

        },
        onRemove: function (tag) {

        }
    };
    options = $.extend(defaultOptions, options);

    var self = this;
    var tags = {};

    this.tagsDiv = $("<div></div>");
    this.tagsDiv.css("display", "inline-block");

    this.input = $("<input type='text'/>");

    this.getTags = function () {
        var toReturn = [];
        $.each(tags, function (i, tag) {
            toReturn.push(tag.tag);
        });
        return toReturn;
    };

    this.addAll = function (tags) {
        $.each(tags, function (i, tag) {
            self.add(tag);
        });
    };

    this.remove = function (tag) {
        tags[options.idValue(tag)].ui.remove();
        delete tags[options.idValue(tag)];

        if (options.onRemove !== undefined) {
            options.onRemove(tag);
        }
    };

    this.add = function (tag) {
        if (self.contains(tag)) {
            console.log("Already contains " + tag);
        } else {
            var remove = $("<span data-role='remove'></span>");
            remove.on("click", function () {
                self.remove(tag);
            });

            var tagUi = options.buildTag(tag);
            tagUi.append(remove);
            this.tagsDiv.append(tagUi);

            tags[options.idValue(tag)] = {
                tag: tag,
                ui: tagUi
            };

            if (options.onAdd !== undefined) {
                options.onAdd(tag);
            }
        }
    };

    this.contains = function (tag) {
        return $.inArray(self.getTags(), tag) >= 0;
    };

    this.wrapper = $("<div></div>");
    this.wrapper.addClass("bootstrap-tagsinput");

    this.wrapper.append(this.tagsDiv);
    this.wrapper.append(this.input);
}
