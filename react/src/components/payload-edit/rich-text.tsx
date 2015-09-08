import {Component, ReactElement} from 'react';
import {Note} from "../../models/note-model";
import * as Remarkable from "jonschlinkert/remarkable";
import {payloadActionCreator} from "../../actions/payload-action-creator";
import {Tags} from "../tags";

interface State {
    unsavedPayload: string;
}

interface Props {
  note : Note;
}

export class RichTextPayload extends Component<Props, State> {

  constructor() {
    super();
    this.state = {
        unsavedPayload: "props not loaded yet... This should never be shown to the user.."
    }
  }

  componentWillMount() {
      this.setState({unsavedPayload: this.props.note.payload});
  }

  componentWillReceiveProps(newProps : Props) {
      this.setState({unsavedPayload: newProps.note.payload});
  }

  private onTextAreaChanged = () => {
      var a : any = React.findDOMNode(this.refs["ta"]);
      this.setState({unsavedPayload: a.value});
  }

  private save = () => {
    payloadActionCreator.updatePayload(this.props.note, this.state.unsavedPayload);
  }

  private render() : ReactElement<any> {
    let md = new Remarkable();
    let unsafeHtml = { __html: md.render(this.state.unsavedPayload) };
    let renderedPayload = <div dangerouslySetInnerHTML={unsafeHtml} />

    let editablePayload = <textarea ref="ta" onChange={this.onTextAreaChanged} value={this.state.unsavedPayload} style={{width: "100%", height: "100%"}} />

    let hasUnsavedChanges = this.props.note.payload !== this.state.unsavedPayload;
    return (
      <div style={{height: "100%"}}>

        <div className="col-md-6" style={{height: "100%"}}>
            {editablePayload}

            <div>
                <button onClick={this.save}
                        disabled={!hasUnsavedChanges}
                        className="btn btn-default"
                        style={{float: "left", height: "43px", marginRight: "5px"}}>Save</button>
                <Tags note={this.props.note} style={{float: "left"}}/>
            </div>
        </div>
        <div className="col-md-6">{renderedPayload}</div>
      </div>
    );
  }
}
