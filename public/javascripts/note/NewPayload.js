function NewPayload(payload) {
    var wrapper = $("<div></div>");
    var viewWrapper = $("<div></div>");
    var currentView;

    setView(new RichTextPayload(payload));

    function setView(view) {
        currentView = view;
        viewWrapper.empty();
        viewWrapper.append(view.ui);
    }

    wrapper.append(viewWrapper);
    return wrapper;
}

function EditButtonWrapper(toWrap) {

    var wrapper = $("<div></div>");
    var viewWrapper = $("<div></div>");
    viewWrapper.css("display", "inline-block");
    viewWrapper.css("margin-right", "0.4em");
    viewWrapper.css("width", "10em");

    function enterUneditableMode() {
       toWrap.getUneditable().show();
       toWrap.getEditable().hide();
    }

    function enterEditMode() {
       toWrap.getUneditable().hide();
       toWrap.getEditable().show();
    }

    function toggleMode() {
        if (toWrap.getEditable().is(":visible")) {
            enterUneditableMode();
        } else {
            enterEditMode();
        }
    }

    function addEditButton() {
        var btn = $("<button>e</button>");
        btn.on("click", toggleMode);

        wrapper.append(btn);
    }

    viewWrapper.append(toWrap.getUneditable());
    viewWrapper.append(toWrap.getEditable());
    wrapper.append(viewWrapper);
    addEditButton();

    enterUneditableMode();

    return wrapper;
}

function SimplePayload(payload) {
    var payloadAsText = $("<div></div>");
    payloadAsText.addClass("payload");
    payloadAsText.addClass("text");
    payloadAsText.append(payload);

    var editable = $("<input type='text' placeholder='Payload' />");
    editable.css("width", "100%");

    this.getUneditable = function () {
        return payloadAsText;
    };

    this.getEditable = function () {
        return editable;
    };

    this.ui = new EditButtonWrapper(this);
}

function RichTextPayload(payload) {
    var payloadAsText = $("<div></div>");
    payloadAsText.addClass("payload");
    payloadAsText.addClass("text");
    payloadAsText.append(payload);

    var richInput = $("<textarea placeholder='Payload'></textarea>");

    this.getUneditable = function () {
        return payloadAsText;
    };

    this.getEditable = function () {
        return richInput;
    };

    this.ui = new EditButtonWrapper(this);
}