import * as React from "react";
import * as React from 'react';
import {Note} from "../../models/note-model";
import {SmallTextPayload} from "../payload-edit/small-text";
import * as rmc from "react-masonry-component";

var Masonry = rmc(React);

interface State {
}

interface Props {
    notes : Array<Note>;
    forceOrder: boolean;
}

// Take a look at http://masonry.desandro.com/
export class CardGrid extends React.Component<Props, State> {

    private cardStyle = {
        float: "left",
        border: "1px solid black",
        padding: "1em"
    };

    render() : React.ReactElement<any> {
        if (this.props.forceOrder) {
            this.cardStyle["margin"] = "1em";
        } else {
            this.cardStyle["marginLeft"] = "1em";
            this.cardStyle["marginTop"] = "1em";
        }

        let cards = this.props.notes.map((note : Note) => {
            return (
                <div style={this.cardStyle} key={note.id}>
                <SmallTextPayload note={note} />
                </div>
            );
        });
        if (!this.props.forceOrder) {
            cards = <Masonry options={{transitionDuration: 0, columnWidth: 10}}>{cards}</Masonry>;
        }

        return (
            <div>
                {cards}
            </div>
        );
    }
}
