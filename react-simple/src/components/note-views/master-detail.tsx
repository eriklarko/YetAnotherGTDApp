import * as React from "react";
import {Link} from 'react-router';
import {Note} from "../../models/note-model";
import {NoteView} from "./note-view";
import {OneLineNoteLink} from "./one-line-note-link";

interface State {
}

interface Props {
    notes : Array<Note>;
    linkBase : string;
    selectedNote: number;
}

export class MasterDetailView extends React.Component<Props, State> {

    private findSelectedNote() : Note {
        if (this.props.selectedNote) {
            let note = this.findFirst(this.props.notes, (a: Note) => a.id == this.props.selectedNote);
            if (note != undefined) {
                return note;
            }
        }

         if (this.props.notes.length > 0){
            console.debug("Defaulted to first note in filter");
            return this.props.notes[0];
        } else {
            return undefined;
        }
    }

    private findFirst<T>(array : Array<T>, predicate: (t:T)=>boolean) : T {
        for (let a of array) {
            if (predicate(a)) {
                return a;
            }
        }

        return undefined;
    }

    render() : React.ReactElement<any> {
        let selectedNote = this.findSelectedNote();
        let selectedNoteView : any = null;
        if (selectedNote) {
            selectedNoteView = <NoteView note={selectedNote} />
        } else {
            selectedNoteView = "No selected note yet. Use the list to the left so select one."
        }

        let list = this.props.notes.map(note => {
            return <ListItem note={note} linkBase={this.props.linkBase}/>;
        });

        return (
            <div style={{height: "100%"}}>
            <div className="col-md-1">{list}</div>
                <div className="col-md-10" style={{height: "100%"}}>{selectedNoteView}</div>
            </div>
        );
    }
}

class ListItem extends React.Component<{note: Note, linkBase: string}, {}> {
    render() : React.ReactElement<any> {
        return <div style={{borderBottom: "1px solid #ccc", paddingTop: "1em", paddingBottom:"1em", overflow: "hidden"}}>
            <OneLineNoteLink note={this.props.note} linkBase={this.props.linkBase} />
        </div>;
    }
}
