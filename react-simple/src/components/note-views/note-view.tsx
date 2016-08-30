// Used to show either a rich text edit or a simple edit field based on the note
// passed as a prop
import * as React from "react";
import {Note, NoteType} from "../../models/note-model";
import {RichTextPayload} from "../payload-edit/rich-text";
import {SmallTextPayload} from "../payload-edit/small-text";

interface State {
}

interface Props {
    note : Note;
}

export class NoteView extends React.Component<Props, State> {
    public render(): React.ReactElement<any> {
        let noteView = this.getNoteView();
        if (noteView) {
            let typeToChangeTo = this.props.note.type == NoteType.Short ? NoteType.MarkedUpText : NoteType.Short;
            let f = () => {
                notesActionCreator.changeNoteType(this.props.note, typeToChangeTo);
            }

            let toggleBetweenRichAndSimple = <div onClick={f} style={{cursor: "pointer"}}>
                {typeToChangeTo == NoteType.Short
                    ? "Make note short"
                    : "Make note rich"}
            </div>;

            return <div>
                <div className="col-md-11">
                    {noteView}
                </div>
                <div className="col-md-1">
                   {toggleBetweenRichAndSimple}
                </div>
            </div>;
        } else {
            return <div>Unknown note type {this.props.note.type}</div>
        }
    }

    private getNoteView(): React.ReactElement<any> {

        if (this.props.note.type == NoteType.Short) {
            return <SmallTextPayload note={this.props.note} onSave={(note, payload) => console.log("note-view onSave", note, payload)} />
        } else if (this.props.note.type == NoteType.MarkedUpText) {
            return <RichTextPayload note={this.props.note} />
        } else {
            return null;
        }
    }
}
