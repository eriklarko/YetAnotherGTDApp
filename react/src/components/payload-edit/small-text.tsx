import {Component, ReactElement} from 'react';
import * as TagsInput from "olahol/react-tagsinput";

interface State {
}

interface Props {
  value : string;
  tags : Array<string>;
}

export class SmallTextPayload extends Component<Props, State> {

  public getPayload() : string {
	var payloadNode : any = React.findDOMNode(this.refs["payload"]);
	return payloadNode.value;
  }

  private addTag(tag) {
	console.log("added", tag);
  }

  private removeTag(tag) {
	console.log("removed", tag);
  }

  private render() : ReactElement<any> {
	var inputField = <input type="text" defaultValue={this.props.value} ref="payload" />

    return (
      <div>
        {inputField}
		<br/>
		<TagsInput value={this.props.tags} onTagAdd={this.addTag} onTagRemove={this.removeTag} />
      </div>
    );
  }
}
