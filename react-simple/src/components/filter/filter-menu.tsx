import * as React from "react";
import {Link} from 'react-router';
import {Filter} from "../../models/filter-model";
import {GenericMasterDetailView} from "../generic-master-detail";
import {NewFilter} from "./new-filter";

interface State {
    showNewFilterForm: boolean;
}

interface Props {
    filters : Array<Filter>;
}

export class FilterMenu extends React.Component<Props, State>  {

    constructor() {
        super();
        this.state = {showNewFilterForm: false};
    }

    private toggleNewFilterForm = () => {
        this.setState({showNewFilterForm: !this.state.showNewFilterForm})
    }

    render() : React.ReactElement<any> {
        let starredFilters = this.props.filters.filter((f) => f.starred);
        let list = starredFilters.map(filter => {
            let selected :boolean = false;
            let className = selected ? "active" : ""
            return  <li className={className}>
                        <Link to={"/filters/" + filter.name}>{filter.name}</Link>
                    </li>
        });

        let newFilterCrud = <div></div>;
        if (this.state.showNewFilterForm) {
            newFilterCrud = <NewFilter onAdded={() => this.setState({showNewFilterForm: false})} />
        }
        return <div>
                <nav className="navbar navbar-default">
                    <div className="container-fluid">

                        <div className="navbar-header">
                            <button type="button" className="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                                <span className="sr-only">Toggle navigation</span>
                                <span className="icon-bar"></span>
                                <span className="icon-bar"></span>
                                <span className="icon-bar"></span>
                            </button>
                        </div>

                        <div className="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                            <ul className="nav navbar-nav">
                                {list}
                            </ul>
                            <button className="navbar-form navbar-right" onClick={this.toggleNewFilterForm}>+</button>
                        </div>
                    </div>
                    {newFilterCrud}
                </nav>
                <div>
                    {this.props.children}
                </div>
            </div>
    }
}
