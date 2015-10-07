import {Component, ReactElement} from 'react';
import {Link} from 'react-router';
import {Note} from "../../models/note-model";
import {RichTextPayload} from "../payload-edit/rich-text";

interface State {
	selectedNote: Note;
}

interface Props {
  notes : Array<Note>;
  params : {noteId: Number};
}

export class MasterDetailView extends Component<Props, State> {

  constructor() {
	super();
	this.state = {selectedNote: null}
  }

  private componentWillReceiveProps(newProps: Props) {
	this.setState({selectedNote: this.findFirst(newProps.notes, (a: Note) => a.id == newProps.params.noteId)});
  }

  private findFirst<T>(array : Array<T>, predicate: (T)=>boolean) : T {
	for (let a of array) {
		if (predicate(a)) {
			return a;
		}
	}

	return null;
  }

  private summarize(obj) {
	return obj.summary || obj.payload;
  }

  private render() : ReactElement<any> {
	let selectedNoteView = <RichTextPayload note={this.state.selectedNote} />
	if (this.state.selectedNote == null) {
		selectedNoteView = "No selected note yet. Use the list to the left so select one."
	}

	let list = this.props.notes.map(note => {
		return <div style={{overflow: "hidden"}}>
			<Link to={"/notes/richNote/" + note.id}>
				{this.summarize(note)}
			</Link>
		</div>
	});

    return (
      <div style={{height: "100%"}}>
	    <div className="col-md-1">{list}</div>
        <div className="col-md-11" style={{height: "100%"}}>{selectedNoteView}</div>
      </div>
    );
  }
}
