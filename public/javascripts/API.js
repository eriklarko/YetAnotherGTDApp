function getAllFilters(successCallback) {
    $.ajax({
        url: "/filters",
        type: "GET",
        dataType: "json",
        success: successCallback,
        error: genericAjaxError
    });
}

function addFilter(name, tags, successCallback) {
    $.ajax({
        url: "/filters",
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

function addTagToFilter(filterId, tagName, successCallback) {
    $.ajax({
        url: "/filters/" + filterId + "/tags/" + tagName,
        type: "POST",
        contentType: "application/json",
        success: successCallback,
        error: genericAjaxError
    });
}

function removeTagFromFilter(filterId, tagName, successCallback) {
    $.ajax({
        url: "/filters/" + filterId + "/tags/" + tagName,
        type: "DELETE",
        contentType: "application/json",
        success: successCallback,
        error: genericAjaxError
    });
}

function loadObjectsInFilter(filterId, successCallback) {
    $.ajax({
        url: "/filters/" + filterId,
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

function removeNote(noteId, successCallback) {
    $.ajax({
        url: "/notes/" + noteId,
        type: "DELETE",
        contentType: "application/json",
        success: successCallback,
        error: genericAjaxError
    });
}

function addTagToNote(noteId, tagName) {
    $.ajax({
        url: "/notes/" + noteId + "/tags/" + tagName,
        type: "POST",
        contentType: "application/json",
        error: genericAjaxError
    });
}

function removeTagFromNote(noteId, tagName) {
    $.ajax({
        url: "/notes/" + noteId + "/tags/" + tagName,
        type: "DELETE",
        contentType: "application/json",
        error: genericAjaxError
    });
}

function genericAjaxError(xhr, status, error) {
    alert("ERROR\n" + xhr.responseText);
    console.log(xhr);
    console.log(status);
    console.log(error);
}