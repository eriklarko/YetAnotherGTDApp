import {Component, ReactElement} from 'react';
import {RouteHandler} from 'react-router';

import {MenuComponent} from './menu';
import {CardGrid} from "./note-views/card-grid";
import {Note} from "../models/note-model";
import {noteStore} from "../stores/note-store";

interface State {
    notes: Array<Note>
}

interface Props {
}

export class ContentComponent extends Component<Props, State> {

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
    return (
      <div>
        <div className="container-fluid">
          <div className="row">
            <MenuComponent />
            <div className="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
              <CardGrid notes={this.state.notes} />
              <br/><br/><br/><br/><br/><br/><hr/><br/><br/><br/>
              <RouteHandler />
            </div>
          </div>
        </div>
      </div>
    );
  }

}
