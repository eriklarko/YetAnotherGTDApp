import {Component, ReactElement} from 'react';
import {FilterView} from "./filter-view";
import {Filter} from "../../models/filter-model";
import {filterStore} from "../../stores/filter-store";

interface State {
}

interface Props {
    params: {filterName: string, selectedNote: number};
    filters: Array<Filter>;
}

export class FilterViewController extends Component<Props, State> {

    private findFilterByName(filterName: string, filters: Array<Filter>) : Filter {
        for(let filter of filters) {
            if (filter.name === filterName) {
                return filter;
            }
        }

        return undefined;
    }

    private render() : ReactElement<any> {
        let filter = this.findFilterByName(this.props.params.filterName, this.props.filters);
        if (filter === undefined) {
            return <div>Unknown filter</div>
        }
        return <FilterView filter={filter} selectedNote={this.props.params.selectedNote} />
    }
}
