import {Component, ReactElement} from 'react';
import {Note} from "../../models/note-model";
import {Tags} from "../tags";

interface State {
}

interface Props {
  note : Note;
}

export class SmallTextPayload extends Component<Props, State> {

  public getPayload() : string {
	var payloadNode : any = React.findDOMNode(this.refs["payload"]);
	return payloadNode.value;
  }

  private render() : ReactElement<any> {
	var inputField = <input type="text" defaultValue={this.props.note.payload} ref="payload" />

    return (
      <div>
        {inputField}
		<br/>
		<Tags note={this.props.note} />
      </div>
    );
  }
}
