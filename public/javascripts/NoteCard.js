function NoteCard(note, removeNoteListener) {
    var mainDiv = $("<div></div>");
    mainDiv.addClass("card");

    var tags = $("<div></div>");
    tags.addClass("tagsRow");
    var payload = new Payload(note.payload);

    mainDiv.append(payload);
    mainDiv.append(tags);

    mainDiv.append("<div style='clear: both'></div>");
    return mainDiv;

    function Payload(payload) {
        var removeBtn = $("<a href='#' title='Remove'>x</a>");
        removeBtn.css("float", "right");
        removeBtn.addClass("remove");
        removeBtn.on('click', function () {
            removeNoteListener(note.id);
        });

        var payloadAsText = $("<div></div>");
        payloadAsText.addClass("payload");
        payloadAsText.addClass("text");
        payloadAsText.append(payload);
        
        var payloadAsInput = $("<input type='text' />");
        
        
        var wrapper = $("<div></div>");
        wrapper.append(removeBtn);
        wrapper.append(payloadAsText);
        wrapper.append(payloadAsInput);
        
        
        payloadAsText.on("click", function () {
            payloadAsInput.val(payloadAsText.html());
            payloadAsInput.focus();
            
            payloadAsText.hide();
            payloadAsInput.show();
        });
        
        payloadAsInput.on("focusout", function () {
            payloadAsText.html(payloadAsInput.val());
            
            payloadAsInput.hide();
            payloadAsText.show();
            
            // TODO: alert listener
        });

        return wrapper;
    }
}