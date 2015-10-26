import {Note} from "./note-model";

export class NoteReminder {
    whenToRemind: Date;
    noteToRemindAbout: Note;

    private dismissed : boolean = false;

    public HasBeenDismissed() : boolean {
        return this.dismissed;
    }

    public Dismiss() {
        if (this.dismissed) {
            console.warn("Tried to dismiss already dismissed note reminder", this);
        }
        this.dismissed = true;
    }
}
