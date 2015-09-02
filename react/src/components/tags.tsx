import {Component, ReactElement} from 'react';
import * as TagsInput from "olahol/react-tagsinput";
import {Note} from "../models/note-model";
import {Tag} from "../models/tag-model";
import {tagsActionCreator} from "../actions/tags-action-creator";

interface State {
    tagCompletions: Array<Tag>
}

interface Props {
  note : Note;
}

export class Tags extends Component<Props, State> {

    availableTags = [{name: "a"}, {name: "b"}, {name: "apa"}];

    constructor() {
        super();

        this.state = {
            tagCompletions: []
        };
    }

	private addTag = (tag: string) => {
		tagsActionCreator.addTag(this.props.note, tag);
	}

	private removeTag = (tag: string) => {
		tagsActionCreator.removeTag(this.props.note, tag);
	}

    private updateTypeaheadMatches = (input: string) => {
        let completions;
        if (input === "") {
            completions = [];
        } else {
            completions = this.availableTags.filter(function (availableTag) {
                var tagName = availableTag.name.toLowerCase();

                var matchesAvailableTag = tagName.substr(0, input.length) == input;
                var isAlreadyAdded = false; //this.state.tags.indexOf(availableTag) > -1;

                return matchesAvailableTag && !isAlreadyAdded;
            });
      }

      this.setState({
          tagCompletions: completions
      });
    }

    private clearTypeaheadMatches = () => {
        this.setState({
            tagCompletions: []
        });
    }

    private renderTagCompletion = (tag: Tag) : ReactElement<any> => {
        console.log("Completion", tag.name);
        var addTag = () => {
            var a : any = this.refs["a"];
            a.addTag(tag.name);
        };
        return <span onClick={addTag}>{tag.name}</span>;
    }

    private render() : ReactElement<any> {
        var renderedTagCompletions = this.state.tagCompletions.map(this.renderTagCompletion);

        return (
            <div>
                <TagsInput
                    ref="a"
                    value={this.props.note.tags}
                    onTagAdd={this.addTag}
                    onTagRemove={this.removeTag}

                    // The following are for the typeahead support
                    addOnBlur={false}
                    onChangeInput={this.updateTypeaheadMatches}
                    onChange={this.clearTypeaheadMatches} />

                <div>{renderedTagCompletions}</div>
            </div>
        );
    }
}
