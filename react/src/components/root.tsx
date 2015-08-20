import {Component, ReactElement} from 'react';
import {RouteHandler} from 'react-router';

import {HeaderComponent} from './header';

interface State {
}

interface Props {
}

export class RootComponent extends Component<Props, State> {
  private render() : ReactElement<any> {
    return (
      <div>
        <HeaderComponent title="Taggy" />
        <RouteHandler />
      </div>
     );
  }
}
