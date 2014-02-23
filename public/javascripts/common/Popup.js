function Popup(content, trigger) {

    var popup = $("<div></div>");
    popup.append(content);
    popup.hide();
    popup.css("background-color", "inherit");
    popup.css("position", "absolute");

    $("body").append(popup);

    trigger.on("click", function () {
        var triggerLoc = trigger.offset();
        console.log(triggerLoc);
        popup.css("top", triggerLoc.top + "px");
        popup.css("left", triggerLoc.left + trigger.width() + 25 + "px");

        popup.toggle();
    });

    return popup;
}