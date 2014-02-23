function FilterSearchTreeView(filter, onTreeChanged) {
    var tagsInput = $("<select multiple placeholder='Tags'></select>");
    var tags = findTagsInSearchTree(filter.searchTree);

    var searchTree = $("<div></div>");
    searchTree.css("width", "17em");
    searchTree.append(tagsInput);

    setupTagsBox(tagsInput, {
       onAddTag: function (tag) {

            updateFilter(filter.id, filter.name, getSearchTree(getTagsFromTagsInput(tagsInput)), function (updatedFilter) {
                 var tags = findTagsInSearchTree(updatedFilter.searchTree);
                 $.each(tags, function(i, newTag) {
                   if (newTag.name === tag.name) {
                       tag.id = newTag.id;
                   }
                });
                onTreeChanged(updatedFilter);
            });
        },
        onRemoveTag: function (tag) {
            updateFilter(filter.id, filter.name, getSearchTree(getTagsFromTagsInput(tagsInput)), function (updatedFilter) {
                onTreeChanged(updatedFilter);
            });
        }
    });
    tagsInput.tagsinput("addAll", tags);
    return searchTree;

    function findTagsInSearchTree(searchTree, foundTags) {
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
            return findTagsInSearchTree(searchTree.child, foundTags);
        } else if (searchTree.children !== undefined) {
            $.each(searchTree.children, function(i, child) {
                findTagsInSearchTree(child, foundTags);
            });
        }

        return foundTags;
    }

    function getSearchTree(tags) {
        var tagsOr = {
            type: "or",
            children: []
        };
        $.each(tags, function (i, tag) {
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

    function getTagsFromTagsInput(input) {
        var tags = [];
        $.each(input.tagsinput("items"), function (i, tagWithIffyId) {
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
}

