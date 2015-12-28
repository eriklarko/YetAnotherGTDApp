import {Component, ReactElement} from 'react';
import {RouteHandler} from 'react-router';
import {errorStore, Error} from "../stores/error-store";
import {errorActionCreator} from "../actions/error-action-creator";

interface State {
    errors: Array<Error>;
}

interface Props {
}

export class ErrorList extends Component<Props, State> {

    constructor() {
        super();
        this.state = {errors: errorStore.getErrors()};
    }

    private componentWillMount() : void {
        errorStore.addChangeListener(this.onChange);
    }

    private componentWillUnmount() : void {
        errorStore.removeChangeListener(this.onChange);
    }

    private onChange = () : void => {
        this.setState({errors: errorStore.getErrors()});
    }

    private render() : ReactElement<any> {
        let errors = this.state.errors.map((error) => {
            let dismissError = () => {
                console.log("dismissing", error);
                errorActionCreator.dismissError(error);
            };
            return <div>
                {error.element}
                <a onClick={dismissError}>X</a>
            </div>;
        });
        return <div>
            {errors}
        </div>
    }
}
