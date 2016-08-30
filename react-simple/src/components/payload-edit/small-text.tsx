import * as React from "react";
import {Note} from "../../models/note-model";
import {Tags} from "../tags";

interface State {
  isEditing: boolean;
  unsavedText: string;
}

interface Props {
  note : Note;
  onSave : (note: Note, payload: string) =>  void;
}

export class SmallTextPayload extends React.Component<Props, State> {

  constructor() {
      super();
      this.state = {
          isEditing: false,
          unsavedText: "props not loaded yet... This should never be shown to the user.."
      }
  }

  private getPayload() : string {
    return this.state.unsavedText || this.props.note.payload;
  }

  componentWillMount() {
      this.setUnsavedText(this.props.note.payload);
  }

  componentWillReceiveProps(newProps : Props) {
      this.setUnsavedText(newProps.note.payload);
      this.leaveEditingState();
  }

  private setUnsavedText(text: string): void {
      this.state.unsavedText = text;
  }

  private enterEditingState = () => {
      this.setState({isEditing: true, unsavedText: this.state.unsavedText});
  }

  private leaveEditingState(): void {
      this.state.isEditing = false;
  }

  private onKeyUp = (event: KeyboardEvent) => {
    if (event.keyCode === 13) { // Enter
      this.sendPayloadUpdatedAction();
    } else if (event.keyCode === 27) { // Esc
      this.setState({isEditing: false, unsavedText: this.props.note.payload});
    }
  }

  private onBlur = () => {
      if (this.state.unsavedText === this.props.note.payload) {
        this.setState({isEditing: false, unsavedText: this.props.note.payload});
      }
  }

  private onInputChanged(e : any) {
      var a : any = e.target;
      this.setState({isEditing: this.state.isEditing, unsavedText: a.value});
  }

  private sendPayloadUpdatedAction() {
    this.props.onSave(this.props.note, this.getPayload());
    this.leaveEditingState();
  }

  render() : React.ReactElement<any> {
    if (this.state.isEditing) {
      var payload = <input
                        autoFocus
                        type="text"
                        ref="payload"
                        onBlurCapture={this.onBlur}
                        onKeyUpCapture={this.onKeyUp}
                        onChange={this.onInputChanged}
                        defaultValue={this.props.note.payload} />
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
