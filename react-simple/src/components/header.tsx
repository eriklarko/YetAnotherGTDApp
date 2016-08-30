import * as React from "react";

interface State {
}

interface Props {
    title: string;
}

export class HeaderComponent extends React.Component<Props, State> {

    private onSearchInputKeyUp = (event: KeyboardEvent) => {
        var a : any = React.findDOMNode(this.refs["searchInput"]);
        if (event.keyCode === 13) { // Enter
            this.search(a.value);
        } else if (event.keyCode === 27) { // Esc
            a.value = "";
        }
    }

    private search(query: string) {
        console.log("Searching for", query);
        window.location.href = "#/search/" + query;
    }

    render() : React.ReactElement<any> {
        return (
            <nav className="navbar navbar-inverse navbar-fixed-top">
                <div className="container-fluid">
                    <div className="navbar-header">
                        <button type="button" className="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                            <span className="sr-only">Toggle navigation</span>
                            <span className="icon-bar"></span>
                            <span className="icon-bar"></span>
                            <span className="icon-bar"></span>
                        </button>
                        <a className="navbar-brand" href="#">{this.props.title}</a>
                    </div>
                    <div id="navbar" className="navbar-collapse collapse">
                        <form className="navbar-form navbar-right">
                            <input type="text" ref="searchInput" onKeyUpCapture={this.onSearchInputKeyUp} className="form-control" placeholder="Search tags or notes..." />
                        </form>
                    </div>
                </div>
            </nav>
        );

    }
}
