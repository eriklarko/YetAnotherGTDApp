import {Component, ReactElement} from 'react';
import {RouteHandler} from 'react-router';

import {MenuComponent} from './menu';

interface State {
}

interface Props {
}

export class ContentComponent extends Component<Props, State> {
  private render() : ReactElement<any> {
    return (
      <div>
        <div className="container-fluid">
          <div className="row">
            <MenuComponent />
            <div className="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
              <RouteHandler />
            </div>
          </div>
        </div>
      </div>
    );
  }

}
