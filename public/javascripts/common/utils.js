var tagsBloodhound = new Bloodhound({
    datumTokenizer: function(d) {
        return Bloodhound.tokenizers.whitespace(d.name);
    },
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    limit: 10,
    prefetch: {
        url: window.location.origin + '/tags',
        filter: function(list) {
            return $.map(list, function(tag) {
                return {name: tag.name};
            });
        }
    }
});

tagsBloodhound.initialize();

function setupTagsBox(jQueryObj, options) {

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
    options = $.extend(defaultOptions, options);
    jQueryObj.tagsinput(options);
}

function isNumber(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}

function setUrl(url) {
    window.history.pushState("hej", "Sometitle", url);
}

function moveTo(url) {
    window.location.pathname = url;
}
