function Popup(content, trigger, loc) {
    if (loc === undefined) {
        loc = "left";
    }

    var popup = $("<div></div>");
    popup.append(content);
    popup.hide();
    popup.css("background-color", "inherit");
    popup.css("position", "absolute");

    $("body").append(popup);

    trigger.on("click", function () {
        if (loc === "left") {
            left();
        } else if (loc === "right") {
            right();
        }

        popup.toggle();
    });

    function left() {
        var triggerLoc = trigger.offset();
        popup.css("top", triggerLoc.top + "px");
        popup.css("left", triggerLoc.left + trigger.width() + 25 + "px");
    }

    function right() {
        var triggerLoc = trigger.offset();
        popup.css("top", triggerLoc.top + "px");
        popup.css("left", triggerLoc.left - content.width() - 50 + "px");
    }

    return popup;
}