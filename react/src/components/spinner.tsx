import {Component, ReactElement} from 'react';

interface State {
}

interface Props {
}

export class SpinnerComponent extends Component<Props, State> {
  private render() : ReactElement<any> {
    return (
      <div className="text-center">
        <div className="throbber-loader">
          Loading…
        </div>
      </div>
    );
  }
}
