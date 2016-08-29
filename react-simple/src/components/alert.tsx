import * as React from "react";

export enum AlertType {Success, Info, Warning, Danger}

interface State {
}

interface Props {
  type : AlertType;
  message : string;
  linkText? : string;
  linkCallback? : any;
}

export class AlertComponent extends React.Component<Props, State> {

  private getClassName() : string {
    var cl:string;
    switch(this.props.type) {
      case AlertType.Success: cl = "alert alert-success"; break;
      case AlertType.Info: cl = "alert alert-info"; break;
      case AlertType.Warning: cl = "alert alert-warning"; break;
      case AlertType.Danger: cl = "alert alert-danger"; break;
    }
    return cl;
  }

  render() : React.ReactElement<any> {
    var link : React.ReactElement<any>;
    if(this.props.linkCallback) {
      link = (
        <div className="pull-right">
          <a role="button" onClick={this.props.linkCallback}>Retry</a>
        </div>
      );
    }

    return (
      <div className={this.getClassName()}>
        {this.props.message}{link}
      </div>
    );
  }
}
