import {dispatcher} from '../dispatcher';
import {Filter} from "../models/filter-model";

class FilterActionCreator {

    public addNewFilter(filter: Filter) {
        dispatcher.dispatch({type: "new-filter", filter: filter});
    }

    public removeFilter(filter: Filter) {
        dispatcher.dispatch({type: "remove-filter", filter: Filter});
    }

    public getNotesInFilter(filter: Filter) {
        var notes = [];
        dispatcher.dispatch({type: "notes-in-filter", filter: Filter, notes: notes});
    }
}

export var filterActionCreator : FilterActionCreator = new FilterActionCreator();
