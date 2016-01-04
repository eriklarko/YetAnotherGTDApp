import {Component, ReactElement} from 'react';
import * as TagsInput from "olahol/react-tagsinput";
import {Note} from "../models/note-model";
import {Tag} from "../models/tag-model";
import {tagsActionCreator} from "../actions/tags-action-creator";

interface State {
    tagCompletions: Array<Tag>;
    availableTags: Array<Tag>;
}

interface Props {
  note : Note;
}

// https://github.com/olahol/react-tagsinput
export class Tags extends Component<Props, State> {

    //private availableTags = [{name: "aasf"}, {name: "b"}, {name: "apa"}];
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
            tagCompletions: [],
            availableTags: [],
        };
    }

	private addTag = (tag: string) => {
		tagsActionCreator.addTag(this.props.note, {name: tag});
	}

	private removeTag = (tag: Tag) => {
		tagsActionCreator.removeTag(this.props.note, tag);
	}

    private updateTypeaheadMatches = (input: string) => {
        let completions;
        if (input === "") {
            completions = [];
        } else {
            let a : any = this.refs["a"];
            completions = this.state.availableTags.filter(function (availableTag) {
                var tagName = availableTag.name.toLowerCase();

                var matchesAvailableTag = tagName.substr(0, input.length) == input;
                var isAlreadyAdded = a.getTags().indexOf(tagName) > -1;

                return matchesAvailableTag && !isAlreadyAdded;
            });
      }

      this.setState({
          tagCompletions: completions,
          availableTags: this.state.availableTags,
      });
    }

    private clearTypeaheadMatches = () => {
        this.setState({
            tagCompletions: [],
            availableTags: this.state.availableTags,
        });
    }

    private renderTagCompletion = (tag: Tag) : ReactElement<any> => {
        var addTag = () => {
            var a : any = this.refs["a"];
            a.addTag(tag.name);
        };
        return <span onClick={addTag} className={this.tagStyles.tag} style={{cursor: "pointer"}}>{tag.name}</span>;
    }

    private renderTag = (index: any, tag: Tag) => {
        let tagIterator = tag;
        let tagText : string = tag.name;
        while (tagIterator.parent) {
            tagText = tagIterator.parent.name + " > " + tagText;
            tagIterator = tagIterator.parent;
        }

        let removeTag = () => this.removeTag(tag);
        return (<span className={this.tagStyles.tag}>
            <span>{tagText}</span>
            <a className={this.tagStyles.remove} onClick={removeTag}></a>
        </span>);
    }

    private render() : ReactElement<any> {
        var renderedTagCompletions = this.state.tagCompletions.map(this.renderTagCompletion);

        return (
            <div>
                <TagsInput
                    ref="a"
                    classNames={this.tagStyles}
                    value={this.props.note.tags}
                    onTagAdd={this.addTag}
                    onTagRemove={this.removeTag}
                    renderTag={this.renderTag}

                    // The following are for the typeahead support
                    addOnBlur={false}
                    onChangeInput={this.updateTypeaheadMatches}
                    onChange={this.clearTypeaheadMatches} />

                <div style={{marginTop: "5px"}}>{renderedTagCompletions}</div>
            </div>
        );
    }
}
