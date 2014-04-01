function NewPayload(options) {
    var self = this;
    var defaultOptions = {
        payload: "",
        defaultMode: "editable", /* un|editable */
        onSubmit: function (value) {
            console.log(value);
        },
        editors: undefined
    };
    options = $.extend(defaultOptions, options);

    if (options.editors === undefined) {
        options.editors = [];
        options.editors.push(new SimplePayload(options));
        options.editors.push(new RichTextPayload(options));
    }
    var currentEditor = -1;

    var wrapper = $("<div></div>");
    var viewWrapper = $("<div></div>");
    $.each(options.editors, function (i, editor){
        viewWrapper.append(editor.ui);
        editor.ui.hide();
    });
    var currentView;

    this.ui = wrapper;
    this.val = function () {
        return currentView.getValue();
    };
    this.setValue = function (value) {
        $.each(options.editors, function (i, editor) {
            editor.setValue(value);
        });
    };
    this.empty = function () {
        currentView.empty();
    };

    this.nextView = function () {
        currentEditor = (currentEditor + 1) % options.editors.length;
        setView(options.editors[currentEditor]);
    };

    function setView(view) {
        if (currentView !== undefined) {
            currentView.ui.hide();
        }
        view.ui.show();
        currentView = view;
    }

    var oldOnSubmit = options.onSubmit;
    options.onSubmit = function (value) {
        self.setValue(value);
        oldOnSubmit(value);
    };

    var viewSwitcherButton = $("<button>..</button>");
    viewSwitcherButton.on("click", this.nextView);

    wrapper.append(viewWrapper);
    wrapper.append(viewSwitcherButton);

    this.nextView();

}

function EditButtonWrapper(toWrap, options) {

    var oldOnSubmit = options.onSubmit;
    options.onSubmit = function (value) {
        enterUneditableMode();
        oldOnSubmit(value);
    };

    var wrapper = $("<div></div>");
    var viewWrapper = $("<div></div>");
    viewWrapper.css("display", "inline-block");
    viewWrapper.css("margin-right", "0.4em");
    viewWrapper.css("width", "10em");

    function enterUneditableMode() {
        toWrap.getEditable().hide();
        toWrap.getUneditable().show();
    }

    function enterEditMode() {
        toWrap.getUneditable().hide();
        toWrap.getEditable().show();
        toWrap.getEditable().focus();
    }

    function toggleMode() {
        if (toWrap.getEditable().is(":visible")) {
            enterUneditableMode();
        } else {
            enterEditMode();
        }
    }

    toWrap.getUneditable().on("click", toggleMode);

    viewWrapper.append(toWrap.getUneditable());
    viewWrapper.append(toWrap.getEditable());
    wrapper.append(viewWrapper);

    if (options.defaultMode === "editable") {
        enterEditMode();
    } else {
        enterUneditableMode();
    }

    return wrapper;
}

function SimplePayload(options) {
    var self = this;
    var payloadAsText = $("<div></div>");
    payloadAsText.addClass("payload");
    payloadAsText.addClass("text");

    var editable = $("<input type='text' placeholder='Payload' />");
    editable.css("width", "100%");
    editable.on("blur", function() {
        self.setValue(editable.val());
        options.onSubmit(self.getValue());
    });

    this.getUneditable = function () {
        return payloadAsText;
    };

    this.getEditable = function () {
        return editable;
    };

    this.getValue = function () {
        return editable.val();
    };
    this.setValue = function (value) {
        editable.val(value);
        payloadAsText.html(value);
    };

    this.empty = function () {
        editable.val("");
    };

    this.setValue(options.payload);
    this.ui = new EditButtonWrapper(this, options);
}

function RichTextPayload(options) {
    var self = this;
    var payloadAsText = $("<div></div>");
    payloadAsText.addClass("payload");
    payloadAsText.addClass("text");

    var richInput = $("<textarea placeholder='Payload'></textarea>");
    richInput.on("blur", function() {
        self.setValue(richInput.val());
        options.onSubmit(self.getValue());
    });

    this.getUneditable = function () {
        return payloadAsText;
    };

    this.getEditable = function () {
        return richInput;
    };

    this.getValue = function () {
        return richInput.val();
    };
    this.setValue = function (value) {
        console.log("VLAUE: " + value);
        payloadAsText.html(value);
        richInput.val(value);
    };

    this.empty = function () {
        richInput.val("");
    };

    this.setValue(options.payload);
    this.ui = new EditButtonWrapper(this, options);
}