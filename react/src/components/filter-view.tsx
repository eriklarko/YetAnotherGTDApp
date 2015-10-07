import {ReactElement, Component} from 'react';
import {RouteHandler} from 'react-router';

import {Filter, DisplayType, SearchTree} from "../models/filter-model";

import {Note} from "../models/note-model";
import {noteStore} from "../stores/note-store";

import {CardGrid} from "./note-views/card-grid";
import {MasterDetailView} from "./note-views/master-detail";

interface State {
    notes: Array<Note>
}

interface Props {
    filter : Filter;
    params: {selectedNote: number}
}

// This class should not exist :) I need to move the stores down into the components that need them.
export class FilterView extends Component<Props, State> {

  constructor() {
      super();
      this.state = {
          notes: []
      };
  }

  private componentWillMount() : void {
    this.onChange();
    noteStore.addChangeListener(this.onChange);
  }

  private componentWillUnmount() : void {
    noteStore.removeChangeListener(this.onChange);
  }

  private onChange = () : void => {
    this.setState({
        notes: this.getFilteredNotes(noteStore.getNotes(), this.props.filter.searchTree)
    });
  }

  private getFilteredNotes(notes: Array<Note>, searchTree: SearchTree) {
      //searchTree
      return notes;
  }

  private render() : ReactElement<any> {
      let notesView = null;
      if (this.props.filter.displayType === DisplayType.Cards) {
          notesView = <CardGrid notes={this.state.notes} />
      } else if (this.props.filter.displayType === DisplayType.MasterDetail) {
          notesView = <MasterDetailView notes={this.state.notes} linkBase={"/filters/" + this.props.filter.name} selectedNote={this.props.params.selectedNote}/>
      } else {
          notesView = <div>Unknown display type {this.props.filter.displayType}</div>
      }

      return (
          <div>
            <h1>{this.props.filter.name}</h1>
            {notesView}
          </div>
      )
  }
}
