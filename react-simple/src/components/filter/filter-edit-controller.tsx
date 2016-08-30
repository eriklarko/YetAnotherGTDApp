import * as React from "react";
import {FilterEdit} from "./filter-edit";
import {Filter} from "../../models/filter-model";

interface State {
}

interface Props {
    params: {filterId: number};
    filters: Array<Filter>;
}

export class FilterEditController extends React.Component<Props, State> {

    private findFilterById(filterId: number, filters: Array<Filter>) : Filter {
        for (let filter of filters) {
            if (filter.id == filterId) {
                return filter;
            }
        }

        return undefined;
    }

    render() : React.ReactElement<any> {
        let filter = this.findFilterById(this.props.params.filterId, this.props.filters);
        if (filter === undefined) {
            console.warn("Trying to render edit form for unknown filter", this.props);
            return <div>Unknown filter</div>
        }
        return <FilterEdit filter={filter} />
    }
}
