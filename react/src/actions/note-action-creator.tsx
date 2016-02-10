import {dispatcher} from '../dispatcher';
import {Note, NoteType} from "../models/note-model";
import {taggyJquery} from "../taggy-jquery";

declare var window : any;

class NotesActionCreator {

    public requestAllNotes() : void {
        dispatcher.dispatch({type: "notes-requested"});

        var host : string = window.location.hostname;
        var url : string = "http://".concat(host,":5001","/api/v1/Note/summaries/");

        taggyJquery.getJSON(url)
            .done(function (response) {
                console.log("jquery success", response);
            })
            .fail(function( jqxhr, textStatus, error ) {
                console.log("jquery faiiiil", jqxhr, textStatus, error);
                dispatcher.dispatch({
                    type: "notes-error",
                    text: textStatus + ", " + error,
                    source: "requestNotes",
                });
            });
    }

    public addNote(note: Note) : void {
        taggyJquery.post("/notes", note);
    }

    public changeNoteType(note: Note, newType: NoteType) : void {
        note.type = newType;

        taggyJquery.getJSON("")
            .done(() => {
                dispatcher.dispatch({
                    type: "note-updated",
                    note: note
                });
            })
            .fail(function () {
                dispatcher.dispatch({type: "new-error", element: <div>Failed changing the note. Forever sad and stuff.</div>});
                console.error("changeNoteType failed", arguments);
            });
    }
}

export var notesActionCreator : NotesActionCreator = new NotesActionCreator();
