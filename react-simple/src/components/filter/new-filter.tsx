import * as React from "react";
import {Filter, DisplayType} from "../../models/filter-model";
import {filterActionCreator} from "../../actions/filter-action-creator";
import {FilterCrudForm} from "./filter-crud-form";

interface State {
}

interface Props {
    onAdded: () => void;
}

export class NewFilter extends Component<Props, State> {

    private create = (newFilter: Filter) => {
        filterActionCreator.addNewFilter(newFilter);

        if (this.props.onAdded) {
            this.props.onAdded();
        }
    }

    private render() : ReactElement<any> {

        return <FilterCrudForm
                    headerText="Create new Filter"
                    submitButtonText="Create"
                    submitButtonOnClick={this.create} />
    }
}
