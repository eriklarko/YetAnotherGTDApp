import {dispatcher} from "../dispatcher";
import {NoteReminder} from "../models/note-reminder";

class NoteReminderActionCreator {

    public dismiss(noteReminder: NoteReminder) {
        dispatcher.dispatch({
            type: "dismiss-note-reminder",
            noteReminder: noteReminder
        });
    }
}

export var noteReminderActionCreator : NoteReminderActionCreator = new NoteReminderActionCreator();
