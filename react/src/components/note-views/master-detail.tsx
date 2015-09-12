import {Component, ReactElement} from 'react';
import {Link} from 'react-router';
import {Note} from "../../models/note-model";
import {RichTextPayload} from "../payload-edit/rich-text";

interface State {
	selectedNote: Note;
}

interface Props {
  notes : Array<Note>;
  linkBase : string;
  selectedNote: number;
}

export class MasterDetailView extends Component<Props, State> {

  constructor() {
	super();
	this.state = {selectedNote: null}
  }

  private componentWillReceiveProps(newProps: Props) {
    if (newProps.notes.length === 0) {
        return;
    }

    if (newProps.selectedNote == undefined) {
        this.setState({selectedNote: newProps.notes[0]});
    } else {
	    this.setState({selectedNote: this.findFirst(newProps.notes, (a: Note) => a.id == newProps.selectedNote)});
    }
  }

  private findFirst<T>(array : Array<T>, predicate: (T)=>boolean) : T {
	for (let a of array) {
		if (predicate(a)) {
			return a;
		}
	}

	return null;
  }

  private render() : ReactElement<any> {
	let selectedNoteView = null;
	if (this.state.selectedNote == null) {
		selectedNoteView = "No selected note yet. Use the list to the left so select one."
	} else {
        selectedNoteView = <RichTextPayload note={this.state.selectedNote} />
    }

	let list = this.props.notes.map(note => {
        return <ListItem note={note} linkBase={this.props.linkBase}/>;
	});

    return (
      <div style={{height: "100%"}}>
	    <div className="col-md-1">{list}</div>
        <div className="col-md-11" style={{height: "100%"}}>{selectedNoteView}</div>
      </div>
    );
  }
}

class ListItem extends Component<{note: Note, linkBase: string}, {}> {

    private summarize(obj) {
      return this.ellipse(obj.summary || obj.payload, 18, "...");
    }

    private ellipse(string, maxLength, indicator) {
      string = string.replace("\n", " ");

      if (string.length > maxLength) {
          return string.substring(0, maxLength - indicator.length) + indicator;
      } else {
          return string;
      }
    }

    private render() : ReactElement<any> {
        return <div style={{borderBottom: "1px solid #ccc", paddingTop: "1em", paddingBottom:"1em", overflow: "hidden"}}>
            <Link to={this.props.linkBase + "/" + this.props.note.id}>
                {this.summarize(this.props.note)}
            </Link>
		</div>;
    }
}
