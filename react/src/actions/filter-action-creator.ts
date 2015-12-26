import {dispatcher} from '../dispatcher';
import {Filter} from "../models/filter-model";

class FilterActionCreator {

    public addNewFilter(filter: Filter) : void {
        dispatcher.dispatch({type: "new-filter", filter: filter});
    }

    public removeFilter(filter: Filter) : void {
        dispatcher.dispatch({type: "removed-filter", filter: Filter});
    }

    public unstarFilter(filter: Filter) : void {
        filter.starred = false;
        dispatcher.dispatch({type: "filter-updated", filter: filter});
    }

    public starFilter(filter: Filter) : void {
        filter.starred = true;
        dispatcher.dispatch({type: "filter-updated", filter: filter});
    }

    public updateFilter(oldFilter: Filter, newFilter: Filter) : void {
        dispatcher.dispatch({type: "filter-updated", filter: newFilter});
    }
}

export var filterActionCreator : FilterActionCreator = new FilterActionCreator();
