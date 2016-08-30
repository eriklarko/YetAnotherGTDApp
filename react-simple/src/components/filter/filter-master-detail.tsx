import * as React from "react";
import {Link} from 'react-router';
import {Filter} from "../../models/filter-model";
import {GenericMasterDetailView} from "../generic-master-detail";
import {FilterView} from "./filter-view";

interface State {
}

interface Props {
    filters : Array<Filter>;
    params: any;
}

export class FilterMasterDetail extends Component<Props, State>  {

    private horizontalLayout(list: Array<ReactElement<any>>, detail: ReactElement<any>) : ReactElement<any> {
        return <div style={{height: "100%"}}>

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

                <div style={{height: "100%"}}>{detail}</div>
            </div>
    }

    private render() : ReactElement<any> {
        var starredFilters = this.props.filters.filter((f) => f.starred);

        return <GenericMasterDetailView
                    items={starredFilters}
                    defaultView="select a filter, damnation"

                    getId={(f) => f.name}

                    selectedId={this.props.params.selectedId}
                    selectedViewContructor={(f) => <FilterView filter={f} selectedNote={this.props.params.selectedNote}/>}

                    linkConstructor={(f, selected) => {
                        let className = selected ? "active" : ""
                        return  <li className={className}>
                                    <Link to={"/filters/" + f.name}>{f.name}</Link>
                                </li>
                    }}

                    listDetailRenderer={this.horizontalLayout}
                    />;
    }
}
