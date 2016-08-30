import * as React from "react";

interface State {
}

interface Props {

}

export class LoginComponent extends React.Component<Props, State> {
  render() : React.ReactElement<any> {
    return <div>
      <h1>Login </h1>
      Username: <input></input>
      Password: <input></input>
      <hr/>
      {this.props.children}
    </div>
  }
}
