import * as React from "react";

interface State {
}

interface Props {
}

export class SpinnerComponent extends React.Component<Props, State> {
  render() : React.ReactElement<any> {
    return (
      <div className="text-center">
        <div className="throbber-loader">
          Loading…
        </div>
      </div>
    );
  }
}
