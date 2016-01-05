import {Component, ReactElement} from 'react';
import {Filter, DisplayType} from "../../models/filter-model";
import {filterActionCreator} from "../../actions/filter-action-creator";
import {FilterCrudForm} from "./filter-crud-form";

interface State {
}

interface Props {
    filter: Filter;
}

export class FilterEdit extends Component<Props, State> {

    private save = (editedFilter: Filter) => {
        filterActionCreator.updateFilter(this.props.filter, editedFilter);
    }

    private render() : ReactElement<any> {

        return <FilterCrudForm
                    headerText={"Editing " + this.props.filter.name}
                    submitButtonText="Save"
                    submitButtonOnClick={this.save}
                    prePopulateFilter={this.props.filter} />

    }
}
