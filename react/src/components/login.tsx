import {Component, ReactElement} from 'react';
import {RouteHandler} from 'react-router';

interface State {
}

interface Props {

}

export class LoginComponent extends Component<Props, State> {
  private render() : ReactElement<any> {
    return <div>
      <h1>Login </h1>
      Username: <input></input>
      Password: <input></input>
      <hr/>
      <RouteHandler />
    </div>
  }
}
