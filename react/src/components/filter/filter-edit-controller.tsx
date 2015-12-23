import {Component, ReactElement} from 'react';
import {FilterEdit} from "./filter-edit";
import {Filter} from "../../models/filter-model";
import {filterStore} from "../../stores/filter-store";

interface State {
}

interface Props {
    params: {filterId: string};
    filters: Array<Filter>;
}

export class FilterEditController extends Component<Props, State> {

    private findFilterById(filterName: string, filters: Array<Filter>) : Filter {
        for(let filter of filters) {
            if (filter.name === filterName) {
                return filter;
            }
        }

        return undefined;
    }

    private render() : ReactElement<any> {
        let filter = this.findFilterById(this.props.params.filterId, this.props.filters);
        if (filter === undefined) {
            return <div>Unknown filter</div>
        }
        return <FilterEdit filter={filter} />
    }
}
