import {Component, ReactElement} from 'react';
import {Filter, DisplayType} from "../../models/filter-model";

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
    private render() : ReactElement<any> {

        return <div>
            <h1>Editing {this.props.filter.name}</h1>
            Name: <input
                        autoFocus
                        type="text"
                        ref="name"
                        defaultValue={this.props.filter.name} />
            <br />
            Display type: <select>{this.getDisplayTypeOptions()}</select> <br />
            Search tree: <canvas />

            <button>Save</button>
        </div>
    }
}
