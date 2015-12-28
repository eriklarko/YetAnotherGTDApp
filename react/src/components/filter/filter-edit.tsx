import {Component, ReactElement} from 'react';
import {Filter, DisplayType} from "../../models/filter-model";
import {filterActionCreator} from "../../actions/filter-action-creator";

interface State {
}

interface Props {
    filter: Filter;
}

export class FilterEdit extends Component<Props, State> {

    private getDisplayTypeOptions() : Array<ReactElement<any>> {
        let displayTypeOptions = [];
        for (var displayType in DisplayType) {
            var isValueProperty = parseInt(displayType, 10) >= 0
            if (!isValueProperty) {
                displayTypeOptions.push(<option value={displayType}>{displayType}</option>);
            }
        }

        return displayTypeOptions;
    }

    private save = () => {
        let newFilter : Filter = this.copyObject(this.props.filter);
        newFilter.name = (this.refs["name"] as any).getDOMNode().value;
        newFilter.starred = (this.refs["starred"] as any).getDOMNode().checked;
        //newFilter.displayType = (this.refs["displayType"] as any).value;
        //newFilter.searchTree = (this.refs["name"] as any).value;
        console.log("Saving", this.props.filter, newFilter);

        filterActionCreator.updateFilter(this.props.filter, newFilter);
    }

    private copyObject<T> (object:T): T {
        let objectCopy : T = {} as T;
        for (var key in object) {
            if (object.hasOwnProperty(key)) {
                objectCopy[key] = object[key];
            }
        }
        return objectCopy;
    }

    private render() : ReactElement<any> {

        return <div>
            <h1>Editing {this.props.filter.name}</h1>
            Name: <input
                        autoFocus
                        type="text"
                        ref="name"
                        defaultValue={this.props.filter.name} />
            <br />
            Starred: <input type="checkbox" ref="starred" defaultChecked={this.props.filter.starred} /> <br/>
            Display type: <select>{this.getDisplayTypeOptions()}</select> <br />
            Search tree: <canvas />

            <button onClick={this.save}>Save</button>
        </div>
    }
}
