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

    private availableTags = [{name: "a"}, {name: "b"}, {name: "apa"}];
    private tagStyles = {
        div: "react-tagsinput",
        input: "react-tagsinput-input",
        invalid: "react-tagsinput-invalid",
        validating: "react-tagsinput-validating",
        tag: "react-tagsinput-tag",
        remove: "react-tagsinput-remove"
    };

    constructor() {
        super();

        this.state = {
            tagCompletions: []
        };
    }

	private addTag = (tag: string) => {
		tagsActionCreator.addTag(this.props.note, {name: tag});
	}

	private removeTag = (tag: string) => {
		tagsActionCreator.removeTag(this.props.note, {name: tag});
	}

    private updateTypeaheadMatches = (input: string) => {
        let completions;
        if (input === "") {
            completions = [];
        } else {
            let a : any = this.refs["a"];
            completions = this.availableTags.filter(function (availableTag) {
                var tagName = availableTag.name.toLowerCase();

                var matchesAvailableTag = tagName.substr(0, input.length) == input;
                var isAlreadyAdded = a.getTags().indexOf(tagName) > -1;

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
        var addTag = () => {
            var a : any = this.refs["a"];
            a.addTag(tag.name);
        };
        return <span onClick={addTag} className={this.tagStyles.tag} style={{cursor: "pointer"}}>{tag.name}</span>;
    }

    private render() : ReactElement<any> {
        var renderedTagCompletions = this.state.tagCompletions.map(this.renderTagCompletion);
        var tagNames = this.props.note.tags.map(tag => tag.name);

        return (
            <div>
                <TagsInput
                    ref="a"
                    classNames={this.tagStyles}
                    value={tagNames}
                    onTagAdd={this.addTag}
                    onTagRemove={this.removeTag}

                    // The following are for the typeahead support
                    addOnBlur={false}
                    onChangeInput={this.updateTypeaheadMatches}
                    onChange={this.clearTypeaheadMatches} />

                <div style={{marginTop: "5px"}}>{renderedTagCompletions}</div>
            </div>
        );
    }
}
