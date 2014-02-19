var tagsBloodhound = new Bloodhound({
    datumTokenizer: function(d) {
        return Bloodhound.tokenizers.whitespace(d.name);
    },
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    limit: 10,
    prefetch: {
        url: '/tags',
        filter: function(list) {
            console.log(list);
            return $.map(list, function(tag) {
                return {name: tag.name};
            });
        }
    }
});
tagsBloodhound.initialize();

var defaultOptions = {
    itemValue: "id",
    itemText: "name",
    onAddTag: function(tag) {
    },
    onRemoveTag: function(tag) {
    },
    typeahead: {
        name: 'tags',
        displayKey: 'name',
        source: tagsBloodhound.ttAdapter()
    }
};


function setupTagsBox(jQueryObj, options) {
    options = $.extend(defaultOptions, options);
    jQueryObj.tagsinput(options);
}

function isNumber(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}
