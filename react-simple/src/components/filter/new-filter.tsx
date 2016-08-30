import * as React from "react";
import {Filter, DisplayType} from "../../models/filter-model";
import {FilterCrudForm} from "./filter-crud-form";

interface State {
}

interface Props {
    onAdded: () => void;
}

export class NewFilter extends React.Component<Props, State> {

    private create = (newFilter: Filter) => {
        filterActionCreator.addNewFilter(newFilter);

        if (this.props.onAdded) {
            this.props.onAdded();
        }
    }

    render() : React.ReactElement<any> {

        return <FilterCrudForm
                    headerText="Create new Filter"
                    submitButtonText="Create"
                    submitButtonOnClick={this.create}
                    prePopulateFilter={undefined} />
    }
}
