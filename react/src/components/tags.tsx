import {Component, ReactElement} from 'react';
import * as TagsInput from "olahol/react-tagsinput";
import {Note} from "../models/note-model";
import {tagsActionCreator} from "../actions/tags-action-creator";

interface State {
}

interface Props {
  note : Note;
}

export class Tags extends Component<Props, State> {

	private addTag = (tag: string) => {
		tagsActionCreator.addTag(this.props.note, tag);
	}

	private removeTag = (tag: string) => {
		tagsActionCreator.removeTag(this.props.note, tag);
	}

    private render() : ReactElement<any> {
        return <TagsInput value={this.props.note.tags} onTagAdd={this.addTag} onTagRemove={this.removeTag} />
    }
}
