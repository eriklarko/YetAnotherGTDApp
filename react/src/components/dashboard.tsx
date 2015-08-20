import {Component, ReactElement} from 'react';

interface State {
}

interface Props {
}

export class DashboardComponent extends Component<Props, State> {
  private render() : ReactElement<any> {
    return (
      <div>
        <h1 className="page-header">Dashboard</h1>
        <div>This is the dashboard</div>
      </div>
    );
  }
}
