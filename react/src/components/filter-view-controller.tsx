import {Component, ReactElement} from 'react';
import {FilterView} from "./filter-view";
import {Filter} from "../models/filter-model";
import {filterStore} from "../stores/filter-store";

interface State {
}

interface Props {
    params: {selectedId: string, selectedNote: number};
    filters: Array<Filter>;
}

export class FilterViewController extends Component<Props, State> {

    private findFilterById(filterName: string, filters: Array<Filter>) : Filter {
        for(let filter of filters) {
            if (filter.name === filterName) {
                return filter;
            }
        }

        return undefined;
    }

    private render() : ReactElement<any> {
        let filter = this.findFilterById(this.props.params.selectedId, this.props.filters);
        console.log("a", this.state, this.props);
        if (filter === undefined) {
            return <div>Unknown filter</div>
        }
        return <FilterView filter={filter} selectedNote={this.props.params.selectedNote} />
    }
}
