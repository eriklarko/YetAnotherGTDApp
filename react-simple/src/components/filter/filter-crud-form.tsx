import {Component, ReactElement} from 'react';
import {Filter, DisplayType} from "../../models/filter-model";
import {filterActionCreator} from "../../actions/filter-action-creator";

interface State {
}

interface Props {
    headerText: String;
    submitButtonText: string;
    submitButtonOnClick: (newFilter: Filter) => void;
    prePopulateFilter: Filter;
}

export class FilterCrudForm extends Component<Props, State> {

    private getDisplayTypeOptions() : Array<ReactElement<any>> {
        let displayTypeOptions = [];
        for (var displayType in DisplayType) {
            var isValueProperty = parseInt(displayType, 10) >= 0
            if (isValueProperty) {
                displayTypeOptions.push(<option value={displayType}>{DisplayType[displayType]}</option>);
            }
        }

        return displayTypeOptions;
    }

    private submitOnClick = () => {
        let newFilter : Filter = this.props.prePopulateFilter
            ? this.copyObject(this.props.prePopulateFilter)
            : new Filter();

        newFilter.name = (this.refs["name"] as any).getDOMNode().value;
        newFilter.starred = (this.refs["starred"] as any).getDOMNode().checked;
        newFilter.displayType = parseInt((this.refs["displayType"] as any).getDOMNode().value);
        //newFilter.searchTree = (this.refs["name"] as any).value;

        this.props.submitButtonOnClick(newFilter);
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
        let defaultValues : Filter;
        if (this.props.prePopulateFilter) {
            defaultValues = this.props.prePopulateFilter;
        } else {
            defaultValues = new Filter();
        }

        return <div>
            <h1>{this.props.headerText}</h1>
            Name: <input
                        autoFocus
                        type="text"
                        ref="name"
                        defaultValue={defaultValues.name} />
            <br />
            Starred: <input type="checkbox" ref="starred" defaultChecked={defaultValues.starred} /> <br/>
            Display type: <select ref="displayType">{this.getDisplayTypeOptions()}</select> <br />
            Search tree: <canvas />

            <button onClick={this.submitOnClick}>{this.props.submitButtonText}</button>
        </div>
    }
}
