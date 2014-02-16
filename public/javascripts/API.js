function getAllFilters(successCallback) {
    $.ajax({
        url: "/filter/list.json",
        type: "GET",
        dataType: "json",
        success: successCallback,
        error: genericAjaxError
    });
}

function addFilter(name, tags, successCallback) {
    $.ajax({
        url: "/filter/add",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            tags: tags,
            name: name,
        }),
        success: successCallback,
        error: genericAjaxError
    });
}

function addTagsToFilter(filterId, tagNames, successCallback) {
    $.ajax({
        url: "/filter/addTags",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            filterId: filterId,
            tagNames: tagNames
        }),
        success: successCallback,
        error: genericAjaxError
    });
}

function removeTagFromFilter(filterId, tagName, successCallback) {
    $.ajax({
        url: "/filter/removeTag",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            filterId: filterId,
            tagName: tagName
        }),
        success: successCallback,
        error: genericAjaxError
    });
}

function loadObjectsInFilter(filterId, successCallback) {
    $.ajax({
        url: "/filter/getTaggables",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            filterId: filterId
        }),
        success: successCallback,
        error: genericAjaxError
    });
}

function loadAllObjects(successCallback) {
    $.ajax({
        url: "/taggable/list.json",
        type: "GET",
        dataType: "json",
        success: successCallback,
        error: genericAjaxError
    });
}

function loadObjects(tagNames, successCallback) {
    $.ajax({
        url: "/taggable/tagged",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
           tagNames: tagNames
        }),
        success: successCallback,
        error: genericAjaxError
    });
}

function addTaggable(payload, tags, successCallback) {
    $.ajax({
        url: "/taggable/add",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            tags: tags,
            payload: payload,
        }),
        success: successCallback,
        error: genericAjaxError
    });
}

function genericAjaxError(xhr, status, error) {
    alert("ERROR\n" + xhr.responseText);
    console.log(xhr);
    console.log(status);
    console.log(error);
}

function removeTaggable(taggableId, successCallback) {
    $.ajax({
        url: "/taggable/remove",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            id: taggableId
        }),
        success: successCallback,
        error: genericAjaxError
    });
}

function addTagToTaggable(taggableId, tagNames) {
    $.ajax({
        url: "/taggable/tag",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            taggableId: taggableId,
            tagNames: tagNames
        }),
        error: genericAjaxError
    });
}

function removeTagFromTaggable(taggableId, tagName) {
    $.ajax({
        url: "/taggable/untag",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            taggableId: taggableId,
            tagName: tagName
        }),
        error: genericAjaxError
    });
}