import * as React from "react";

interface State {
}

interface Props {
    params: {query: string}
}

export class Search extends React.Component<Props, State> {

    public render(): ReactElement<any> {
        return <div>Searching for {this.props.params.query}</div>;
    }
}
