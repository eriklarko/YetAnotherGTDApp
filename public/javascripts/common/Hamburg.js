function Hamburg(options) {
    var self = this;
    var defaultOptions = {
        initialState: "collapsed",
        collapseTime: 300,
        expandTime: 300
    };

    self.options = $.extend(defaultOptions, options);
    var state = self.options.initialState;

    function otherState(state) {
        if (state === "out") {
            return "collapsed";
        } else if (state === "collapsed") {
            return "out";
        }
    }

    this.toggle = function () {
        if (state === "out") {
            self.slideIn();
        } else if (state === "collapsed") {
            self.slideOut();
        }
        state = otherState(state);
    };

    this.slideOut = function () {
        var targetWidth = 300;

        self.options.object.show();
        wrapper.animate({width: targetWidth}, self.options.expandTime, function () {
            btn.html("&lt;-");
        });
    };

    this.slideIn = function () {
        var targetWidth = btn.width() + 10;

        wrapper.animate({width: targetWidth}, self.options.collapseTime, function () {
            self.options.object.hide();
            btn.html("-&gt;");
        });
    };

    var wrapper = $("<div></div>");
    wrapper.addClass("hamburger-wrapper");
    wrapper.css("float", "left");
    wrapper.css("position", "absolute");
    wrapper.css("backgroud-color", "inherit");
    wrapper.css("z-index", "1");

    var btn = $("<a></a>");
    btn.css("float", "right");

    btn.append("&lt;-");
    btn.on("click", this.toggle);

    if (state === "collapsed") {
        self.options.object.hide();
        btn.html("-&gt;");
        wrapper.css("width", "21px");
    }

    self.options.object.replaceWith(wrapper);

    wrapper.append(btn);
    wrapper.append("<div style='clear: both'/>")
    wrapper.append(self.options.object);
}
