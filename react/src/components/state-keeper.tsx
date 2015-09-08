import {Component, ReactElement} from 'react';
import {RouteHandler} from 'react-router';

import {MenuComponent} from './menu';
import {CardGrid} from "./note-views/card-grid";
import {MasterDetailView} from "./note-views/master-detail";

import {Note} from "../models/note-model";
import {noteStore} from "../stores/note-store";

interface State {
    notes: Array<Note>
}

interface Props {
}

// This class should not exist :) I need to move the stores down into the components that need them.
export class StateKeeper extends Component<Props, State> {

  constructor() {
      super();
      this.state = {notes: []};
  }

  private componentDidMount() : void {
    noteStore.addChangeListener(this.onChange);
  }

  private componentWillUnmount() : void {
    noteStore.removeChangeListener(this.onChange);
  }

  private onChange = () : void => {
    this.setState({notes: noteStore.getNotes()});
  }

  private render() : ReactElement<any> {
    return <RouteHandler notes={this.state.notes}/>
  }
}
