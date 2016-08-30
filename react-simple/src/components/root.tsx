import * as React from "react";
import {HeaderComponent} from './header';

interface State {
}

interface Props {
}

export class RootComponent extends React.Component<Props, State> {
  render() : React.ReactElement<any> {
    return (
      <div style={{height: "100%"}}>
        <HeaderComponent title="Taggy" />
        {this.props.children}
      </div>
     );
  }
}
