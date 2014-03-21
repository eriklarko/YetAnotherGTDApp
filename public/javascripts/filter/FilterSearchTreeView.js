function FilterSearchTreeView(options) {
    var self = this;
    var defaultOptions = {
        onAddTag: undefined,
        onRemoveTag: undefined,
        onTreeChanged: undefined,
        searchTree: {}
    };
    options = $.extend(defaultOptions, options);

    this.findTagsInSearchTree = function (searchTree, foundTags) {
        if (foundTags === undefined) {
            var foundTags = [];
        }

        if (searchTree.type === "ideq") {
            if (searchTree.name !== "archive") {
                foundTags.push({
                    id: searchTree.id,
                    name: searchTree.name
                });
            }
        } else if (searchTree.child !== undefined) {
            return self.findTagsInSearchTree(searchTree.child, foundTags);
        } else if (searchTree.children !== undefined) {
            $.each(searchTree.children, function(i, child) {
                self.findTagsInSearchTree(child, foundTags);
            });
        }

        return foundTags;
    };

    this.getSearchTree = function () {
        var tagsOr = {
            type: "or",
            children: []
        };
        var tags = getTags();
        $.each(tags, function (i, tag) {
           tagsOr.children.push({
              type: "ideq",
              id: tag.id,
              name: tag.name
           });
        });

        if (false && tags.contains(archive)) {
            return tagsOr;
        } else {
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
    };

    function getTags () {
        var tags = [];
        $.each(tagsInput.tagsinput("items"), function (i, tagWithIffyId) {
            if (!isNumber(tagWithIffyId.id)) {
                var newTag = {
                    name: tagWithIffyId.name
                };
                tags.push(newTag);
            } else {
                tags.push(tagWithIffyId);
            }
        });
        return tags;
    }

    var tagsInput = $("<select multiple placeholder='Tags'></select>");

    this.gui = $("<div></div>");
    this.gui.css("width", "17em");
    this.gui.append(tagsInput);

    setupTagsBox(tagsInput, {
        onAddTag: options.onAddTag,
        onRemoveTag: options.onRemoveTag
    });
    var tags = self.findTagsInSearchTree(options.searchTree);
    tagsInput.tagsinput("addAll", tags);
}

