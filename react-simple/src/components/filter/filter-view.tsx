import * as React from "react";

import {Filter, DisplayType, SearchTree} from "../../models/filter-model";

import {Note} from "../../models/note-model";

import {CardGrid} from "../note-views/card-grid";
import {MasterDetailView} from "../note-views/master-detail";


interface State {
    notes: Array<Note>
}

interface Props {
    filter : Filter;
    selectedNote: number;
    showFilterName: boolean;
}

export class FilterView extends React.Component<Props, State> {

  constructor() {
      super();
      this.state = {
          notes: []
      };
  }


  private getFilteredNotes(notes: Array<Note>, searchTree: SearchTree) {
      console.warn("Viewing a filter. This is a bit weird now because all filters show the same notes :)")
      //searchTree
      return notes;
  }

  private createStarToggle(filter: Filter) {
      var starToggleFunction = () => {
          if(filter.starred) {
              // todo
          } else {
              // todo
          }
      }
      return <button onClick={starToggleFunction}>{filter.starred ? "unstar filter" : "star filter"}</button>;
      /*return <div>
          <div style={{float: "inline"}}>
              {view}
          </div>
          <div style={{float: "inline"}}>{starToggle}</div>
      </div>*/
  }

  render() : React.ReactElement<any> {
      let notesView : any = null;
      if (this.props.filter.displayType === DisplayType.Cards) {
          notesView = <CardGrid notes={this.state.notes} forceOrder={false}/>
      } else if (this.props.filter.displayType === DisplayType.MasterDetail) {
          notesView = <MasterDetailView
                            notes={this.state.notes}
                            linkBase={"/filters/" + this.props.filter.name}
                            selectedNote={this.props.selectedNote}/>
      } else {
          notesView = <div>Unknown display type {this.props.filter.displayType}</div>
      }

      var filterName : any = "";
      if (this.props.showFilterName) {
          filterName =<h1>{this.props.filter.name}</h1>
      }

      var starToggle = this.createStarToggle(this.props.filter);

      return (
          <div>
              <div className="col-md-11">
                  {filterName}
                  {notesView}
              </div>
              <div className="col-md-1">{starToggle}</div>
          </div>
      )
  }
}
