import {Component, ReactElement} from 'react';
import {FilterView} from "./filter-view";
import {Filter} from "../models/filter-model";
import {filterStore} from "../stores/filter-store";

interface Props {
    params: any;
}

interface State {
    filter: Filter;
}

export class FilterViewController extends Component<Props, State> {
    constructor() {
        super();
        this.state = {filter: undefined};
    }

    private componentWillMount() : void {
      filterStore.addChangeListener(this.onChange);
      this.onChange();
    }

    private onChange = () : void => {
        this.updateFilter(this.props.params.filterId);
    }

    private updateFilter(filterName: string) {
        this.setState({filter: this.findFilterById(filterName, filterStore.getFilters())});
    }

    private findFilterById(filterName: string, filters: Array<Filter>) : Filter {
        for(let filter of filters) {
            if (filter.name === filterName) {
                return filter;
            }
        }

        return undefined;
    }

    private componentWillUnmount() : void {
      filterStore.removeChangeListener(this.onChange);
    }

    private componentWillReceiveProps(newProps: Props) {
        this.updateFilter(newProps.params.filterId);
    }

    private render() : ReactElement<any> {
        if (this.state.filter === undefined) {
            return <div>Unknown filter</div>
        }
        return <FilterView filter={this.state.filter} selectedNote={this.props.params.noteId} />
    }
}
