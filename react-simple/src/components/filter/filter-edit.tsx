import * as React from "react";
import {Filter, DisplayType} from "../../models/filter-model";
import {filterActionCreator} from "../../actions/filter-action-creator";
import {FilterCrudForm} from "./filter-crud-form";

interface State {
    filterRemoved: boolean;
}

interface Props {
    filter: Filter;
}

export class FilterEdit extends Component<Props, State> {

    constructor() {
        super();
        this.state = {
            filterRemoved: false
        }
    }

    private save = (editedFilter: Filter) => {
        filterActionCreator.updateFilter(this.props.filter, editedFilter);
    }

    private removeFilter = () => {
        filterActionCreator.removeFilter(this.props.filter);
        this.setState({filterRemoved: true});
    }

    private render() : ReactElement<any> {
        if (this.state.filterRemoved) {
            return <h2>The filter is removed</h2>;
        }

        return <div>
                <FilterCrudForm
                    headerText={"Editing " + this.props.filter.name}
                    submitButtonText="Save"
                    submitButtonOnClick={this.save}
                    prePopulateFilter={this.props.filter} />
                <button onClick={this.removeFilter}>DELETE FILTER</button>
        </div>;
    }
}
