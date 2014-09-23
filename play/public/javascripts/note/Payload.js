function Payload() {
    var self = this;
    var richId = ("asdasdasd" + Math.random()).replace(".", "");

    var simpleInput = $("<input type='text' placeholder='Payload' />");
    simpleInput.css("width", "100%");

    var richInput = $("<textarea placeholder='Payload'></textarea>");
    richInput.attr("id", richId);
    var switchModeButton = $("<button></button>");

    var inputWrapper = $("<div></div>");
    inputWrapper.append(simpleInput);
    inputWrapper.append(richInput);
    inputWrapper.append(switchModeButton);

    setupModeSwitcher();
    this.ui = inputWrapper;


    function setupModeSwitcher() {

        var initted = false;
        setSimpleMode();

        function setRichTextMode() {
            self.val =  function () {
                return tinyMCE.get(richId).getContent();
            };
            self.empty = function () {
                tinyMCE.get(richId).setContent("");
            };

            if (!initted) {
                richInput.show();
                tinymce.init({
                    selector: "#" + richId,
                    statusbar: false
                });

                initted = true;
            }

            $("#mce_14").show();
            simpleInput.hide();

            switchModeButton.unbind("click", setRichTextMode);
            switchModeButton.on("click", setSimpleMode);
            switchModeButton.html("To simple");
        }

        function setSimpleMode() {
            self.val =  function () {
                return simpleInput.val();
            };
            self.empty = function () {
                simpleInput.val("");
            };

            richInput.hide();
            $("#mce_14").hide();
            simpleInput.show();

            switchModeButton.unbind("click", setSimpleMode);
            switchModeButton.on("click", setRichTextMode);
            switchModeButton.html("To rich");
        }
    }

    function SimpleMode() {

        var input = $("<input type='text' placeholder='Payload' />");
        input.css("width", "100%");

        this.enter = function (parent) {
            parent.val =  function () {
                return input.val();
            };
            parent.empty = function () {
                input.val("");
            };

            input.show();
        };

        this.leave = function (parent) {

        };

        this.show = function () {
            var payloadAsText = $("<div></div>");
            payloadAsText.addClass("payload");
            payloadAsText.addClass("text");
            payloadAsText.append(payload);
            payloadAsText.editable({
                type: 'text',
                showbuttons: false,
                mode: "inline",

                url: function (params) {
                    var deferred = new $.Deferred;
                    updatePayload(note.id, params.value,
                        function () {
                            deferred.resolve();
                        }, function (xhr) {
                            deferred.reject(xhr);
                    });

                    return deferred.promise();
                }
            });
        };
    }

    function RichTextMode() {

    }
}

