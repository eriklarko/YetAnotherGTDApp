import {Component, ReactElement} from 'react';
import {Link, RouteHandler} from 'react-router';
import {Filter} from "../models/filter-model";
import {GenericMasterDetailView} from "./generic-master-detail";
import {FilterView} from "./filter-view";

interface State {
}

interface Props {
    filters : Array<Filter>;
}

export class FilterMenu extends Component<Props, State>  {

    private render() : ReactElement<any> {
        let starredFilters = this.props.filters.filter((f) => f.starred);
        let list = starredFilters.map(filter => {
            let selected :boolean = false;
            let className = selected ? "active" : ""
            return  <li className={className}>
                        <Link to={"/filters/" + filter.name}>{filter.name}</Link>
                    </li>
        });
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
                        </div>
                    </div>
                </nav>
                <div>
                    <RouteHandler {...this.props} />
                </div>
            </div>
    }
}
