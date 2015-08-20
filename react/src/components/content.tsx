import {Component, ReactElement} from 'react';
import {RouteHandler} from 'react-router';

import {MenuComponent} from './menu';
import {CardGrid} from "./note-views/card-grid";
import {Note} from "../models/note-model";

interface State {
}

interface Props {
}

export class ContentComponent extends Component<Props, State> {
  private render() : ReactElement<any> {
    var notes : Array<Note> = [{
        id: 1,
        payload: "HEJSAN",
        tags: ["tag1", "tag2"]
    },{
        id: 1,
        payload: "asdsad",
        tags: ["123", "567"]
    }];

    return (
      <div>
        <div className="container-fluid">
          <div className="row">
            <MenuComponent />
            <div className="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
              <CardGrid notes={notes} />
              <hr/>
              <RouteHandler />
            </div>
          </div>
        </div>
      </div>
    );
  }

}
