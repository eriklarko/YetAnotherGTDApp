import {Component, ReactElement} from 'react';
import {Note} from "../../models/note-model";
import {SmallTextPayload} from "../payload-edit/small-text";

interface State {
}

interface Props {
  notes : Array<Note>;
}

// Take a look at http://masonry.desandro.com/
export class CardGrid extends Component<Props, State> {

    private cardStyle = {
		float: "left",
		border: "1px solid black",
		margin: "1em",
		padding: "1em"
	};

  private render() : ReactElement<any> {
	var cards = this.props.notes.map((note : Note) => {
        return (
          <div style={this.cardStyle} key={note.id}>
			<SmallTextPayload note={note} />
		  </div>
        );
    });

    return (
      <div>
        {cards}
      </div>
    );
  }
}
