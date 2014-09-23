function NewFilterView(options) {

    var name = $("<input type='text' placeholder='Filter name' />");
    name.css("width", "100%");

    var tags = $("<select multiple placeholder='Tags'></select>");

    var addBtn = $("<button>Create</button>");
    addBtn.css("float", "right");
    addBtn.on("click", function() {
        addFilter(name.val(), getSearchTree(), function (newFilter) {
            clearInput();

            options.onCreate(newFilter);
        });
    });

    var wrapper = $("<fieldset></fieldset>");
    wrapper.css("width", "17em");
    wrapper.append("<legend>New filter</legend>");
    wrapper.append(name);
    wrapper.append("<div class='line-separator'></div>");
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
