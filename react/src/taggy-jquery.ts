import * as jquery from "jquery";

enum SIMIAN_MODE {
    FAIL_INSTANTLY, FAIL_AFTER_DELAY,
    ADD_DELAY, NONE,
}

class TaggyJquery {

    private simianMode : SIMIAN_MODE = SIMIAN_MODE.NONE;

    public getJSON(url: string) : JQueryXHR  {
        if (this.simianMode === SIMIAN_MODE.NONE) {
            return jquery.getJSON(url);
        } else {
            return this.getStubbedXHR(this.simianMode, url, jquery.getJSON);
        }
    }

    public post(url: string, payload: any) {
        if (this.simianMode === SIMIAN_MODE.NONE) {
            return jquery.post(url, payload);
        } else {
            return this.getStubbedXHR(this.simianMode, url, url => jquery.post(url, payload));
        }
    }

    private getStubbedXHR(simianMode: SIMIAN_MODE, url: string, realCallback: (url : string) => any) {
        if (simianMode === SIMIAN_MODE.ADD_DELAY) {
            var realRequest = realCallback(url);
        }

        let stubbedXHR : any = {};
        stubbedXHR.done = function (callback) {

            if (simianMode === SIMIAN_MODE.ADD_DELAY) {
                realRequest.done(function(response) {
                    setTimeout(function () {
                        callback(response);
                    }, 10000);
                });
            }
            return stubbedXHR;
        };

        stubbedXHR.fail = function (callback) {
            stubbedXHR.failCallback = callback;
            if (simianMode === SIMIAN_MODE.FAIL_INSTANTLY) {
                setTimeout(function () {callback("Simian instant failure", "second arg", "third arg");}, 1);
            } else if (simianMode === SIMIAN_MODE.FAIL_AFTER_DELAY) {
                setTimeout(function () {callback("Simian delayed failure", "second arg", "third arg");}, 10000);
            } else if (simianMode === SIMIAN_MODE.ADD_DELAY) {

                realRequest.fail(function(a,b,c) {
                    setTimeout(function () {
                        callback(a,b,"Simian add delay resulted in fail " + c);
                    }, 10000);
                });
            }
            return stubbedXHR;
        };

        return stubbedXHR;
    }
}

export var taggyJquery = new TaggyJquery();
