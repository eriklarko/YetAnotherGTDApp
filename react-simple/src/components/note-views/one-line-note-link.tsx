import * as React from "react";
import {Note} from '../../models/note-model';
import {Link} from 'react-router';


export class OneLineNoteLink extends Component<{note: Note, linkBase: string}, {}> {

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

    render() : React.ReactElement<any> {
        return  <Link to={this.props.linkBase + "/" + this.props.note.id}>
                    {this.summarize(this.props.note)}
                </Link>;
    }
}
