function loadObjects(successCallback) {
    $.ajax({
        url: "/taggable/list.json",
        type: "GET",
        dataType: "json",
        success: successCallback,
        error: genericAjaxError
    });
}

function addTaggable(successCallback) {
    $.ajax({
        url: "/taggable/add",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            tags: getTags(),
            payload: $("#newPayload").val(),
        }),
        success: successCallback,
        error: genericAjaxError
    });
}

function genericAjaxError(xhr, status, error) {
    alert("ERROR");
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