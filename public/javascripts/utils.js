var defaultOptions = {
    url: "/tags",
    itemValue: "id",
    itemText: "name",
    onAddTag: function (tag) {
    },
    onRemoveTag: function (tag) {
    }
    /*,
    typeahead: {
        source: ["aaa", "bbb"]
    }*/
}

function setupTagsBox(jQueryObj, options) {
    options = $.extend(defaultOptions, options);

    jQueryObj.tagsinput(options);
}
