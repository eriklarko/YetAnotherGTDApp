import {Component, ReactElement} from 'react';
import {RouteHandler} from 'react-router';
import {MenuComponent} from "./menu";
import {HeaderComponent} from './header';

interface State {
}

interface Props {
}

export class RootComponent extends Component<Props, State> {
  private render() : ReactElement<any> {
    return (
      <div style={{height: "100%"}}>
        <HeaderComponent title="Taggy" />
        <MenuComponent />

        <hr/>
        <RouteHandler />
      </div>
     );
  }
}
