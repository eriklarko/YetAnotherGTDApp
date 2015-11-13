import {ReactElement, Component} from 'react';
import {RouteHandler} from 'react-router';

import {Filter, DisplayType, SearchTree} from "../models/filter-model";

import {Note} from "../models/note-model";
import {noteStore} from "../stores/note-store";

import {CardGrid} from "./note-views/card-grid";
import {MasterDetailView} from "./note-views/master-detail";

import {filterActionCreator} from "../actions/filter-action-creator";

interface State {
    notes: Array<Note>
}

interface Props {
    filter : Filter;
    selectedNote: number;
    showFilterName: boolean;
}

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

  private createStarToggle(filter: Filter) {
      var starToggleFunction = () => {
          if(filter.starred) {
              filterActionCreator.unstarFilter(filter);
          } else {
              filterActionCreator.starFilter(filter);
          }
      }
      return <button onClick={starToggleFunction}>{filter.starred ? "unstar" : "star"}</button>;
      /*return <div>
          <div style={{float: "inline"}}>
              {view}
          </div>
          <div style={{float: "inline"}}>{starToggle}</div>
      </div>*/
  }

  private render() : ReactElement<any> {
      let notesView = null;
      if (this.props.filter.displayType === DisplayType.Cards) {
          notesView = <CardGrid notes={this.state.notes} />
      } else if (this.props.filter.displayType === DisplayType.MasterDetail) {
          notesView = <MasterDetailView
                            notes={this.state.notes}
                            linkBase={"/filters/" + this.props.filter.name}
                            selectedNote={this.props.selectedNote}/>
      } else {
          notesView = <div>Unknown display type {this.props.filter.displayType}</div>
      }

      var filterName = "";
      if (this.props.showFilterName) {
          filterName =<h1>{this.props.filter.name}</h1>
      }

      var starToggle = this.createStarToggle(this.props.filter);

      return (
          <div>
              <div style={{display: "inline"}}>
                  {filterName}
                  {notesView}
              </div>
              <div style={{display: "inline"}}>{starToggle}</div>
          </div>
      )
  }
}
