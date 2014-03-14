function getAllFilters(successCallback) {
    $.ajax({
        url: "/filters",
        type: "GET",
        dataType: "json",
        success: successCallback,
        error: genericAjaxError
    });
}

function addFilter(name, searchTree, successCallback) {
    $.ajax({
        url: "/filters",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            name: name,
            searchTree: searchTree
        }),
        success: successCallback,
        error: genericAjaxError
    });
}

function updateFilter(filterId, name, searchTree, successCallback) {
    $.ajax({
        url: "/filters/" + filterId,
        type: "PUT",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            name: name,
            searchTree: searchTree
        }),
        success: successCallback,
        error: genericAjaxError
    });
}

function loadObjectsInFilter(filterId, successCallback) {
    $.ajax({
        url: "/filters/" + filterId + "/notes",
        type: "GET",
        contentType: "application/json",
        success: successCallback,
        error: genericAjaxError
    });
}

function loadAllObjects(successCallback) {
    $.ajax({
        url: "/notes",
        type: "GET",
        dataType: "json",
        success: successCallback,
        error: genericAjaxError
    });
}

function addNote(payload, tags, successCallback) {
    $.ajax({
        url: "/notes",
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

function updatePayload(noteId, payload, successCallback, errorCallback) {
    $.ajax({
        url: "/notes/" + noteId,
        type: "PUT",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            payload: payload
        }),
        success: successCallback,
        error: errorCallback || genericAjaxError
    });
}

function removeNote(noteId, successCallback) {
    $.ajax({
        url: "/notes/" + noteId,
        type: "DELETE",
        success: successCallback,
        error: genericAjaxError
    });
}

function addTagToNote(noteId, tagName, successCallback) {
    $.ajax({
        url: "/notes/" + noteId + "/tags/" + tagName,
        type: "POST",
        success: successCallback,
        error: genericAjaxError
    });
}

function removeTagFromNote(noteId, tagName, successCallback) {
    $.ajax({
        url: "/notes/" + noteId + "/tags/" + tagName,
        type: "DELETE",
        success: successCallback,
        error: genericAjaxError
    });
}

function updateTagName(tagId, tagName, successCallback, errorCallback) {
    $.ajax({
        url: "/tags/" + tagId,
        type: "PUT",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            name: tagName
        }),
        success: successCallback,
        error: errorCallback || genericAjaxError
    });
}

function searchPayloadsAndTagNames(query, successCallback) {
    $.ajax({
        url: "/search/payloadsAndTagNames/" + query,
        type: "GET",
        dataType: "json",
        success: successCallback,
        error: genericAjaxError
    });
}

function genericAjaxError(xhr, status, error) {
    alert("ERROR\n" + xhr.responseText);
    console.log(xhr);
}