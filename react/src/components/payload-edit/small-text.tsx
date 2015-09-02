import {Component, ReactElement, KeyboardEvent} from 'react';
import {Note} from "../../models/note-model";
import {Tags} from "../tags";
import {payloadActionCreator} from "../../actions/payload-action-creator";

interface State {
    isEditing: boolean;
}

interface Props {
  note : Note;
}

export class SmallTextPayload extends Component<Props, State> {

  constructor() {
      super();
      this.state = {
          isEditing: false
      }
  }

  private getPayload() : string {
    let payloadNode : any = React.findDOMNode(this.refs["payload"]);
    return payloadNode.value;
  }

  private enterEditingState = () => {
      this.setState({isEditing: true});
  }

  private onKeyUp = (event: KeyboardEvent) => {
    // Enter is pressed
    if (event.keyCode === 13) {
      this.sendPayloadUpdatedAction();
    }
  }

  private onBlur = () => {
      console.log("OnBlur");
      //sendPayloadUpdatedAction();
  }

  private sendPayloadUpdatedAction() {
    payloadActionCreator.updatePayload(this.props.note, this.getPayload());
  }

  private render() : ReactElement<any> {
    if (this.state.isEditing) {
      var payload = <input type="text" onBlurCapture={this.onBlur} onKeyUpCapture={this.onKeyUp} defaultValue={this.props.note.payload} ref="payload" />
    } else {
      var payload = <div onClick={this.enterEditingState}>{this.props.note.payload}</div>
    }

    return (
      <div>
        {payload}
		<br/>
		<Tags note={this.props.note} />
      </div>
    );
  }
}
