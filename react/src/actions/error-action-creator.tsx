import {dispatcher} from '../dispatcher';
import {Error} from "../stores/error-store";

class ErrorActionCreator {

    public dismissError(error: Error) : void {
        dispatcher.dispatch({
            type: "dismiss-error",
            error: error
        });
    }
}

export var errorActionCreator : ErrorActionCreator = new ErrorActionCreator();
