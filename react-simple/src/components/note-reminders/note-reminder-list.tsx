import * as React from "react";
import {NoteReminder} from "../../models/note-reminder";
import {OneLineNoteLink} from "../note-views/one-line-note-link";

interface Props {
    noteReminders: Array<NoteReminder>;
    onDismiss: (NoteReminder) => void;
}

interface State {
}

export class NoteReminderList extends React.Component<Props, State> {

    render() : ReactElement<any> {
        let items = this.props.noteReminders.map(nr => {
            let onClose = () => {
                if (this.props.onDismiss) {
                    this.props.onDismiss(nr);
                }
            };

            return  <li className="alert">
                        <button type="button" className="close" aria-label="Close" onClick={onClose}>
                            <span aria-hidden="true">&times;</span>
                        </button>
                        <OneLineNoteLink note={nr.noteToRemindAbout} linkBase="/note"/>
                    </li>
        });

        return (
            <ul>{items}</ul>
        );
    }
}
