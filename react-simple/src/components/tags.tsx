import * as React from "react";
import * as TagsInput from "olahol/react-tagsinput";
import {Note} from "../models/note-model";
import {Tag, tagEquals} from "../models/tag-model";
import {tagsActionCreator} from "../actions/tags-action-creator";
import {noteStore} from "../stores/note-store";

interface State {
    tagCompletions: Array<Tag>;
    availableTags: Array<Tag>;
}

interface Props {
  note : Note;
}

// https://github.com/olahol/react-tagsinput
export class Tags extends Component<Props, State> {

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

    private componentWillMount() : void {
        noteStore.addChangeListener(this.notesChanged);
        this.setState({
            tagCompletions: this.state.tagCompletions,
            availableTags: noteStore.getAllTags(),
        });
    }

    private componentWillUnmount() : void {
        noteStore.removeChangeListener(this.notesChanged);
    }

    private notesChanged = () => {
        this.setState({
            tagCompletions: this.state.tagCompletions,
            availableTags: noteStore.getAllTags(),
        })
    }

    private updateTypeaheadMatches = (input: string) => {
        let completions;
        if (input === "") {
            completions = [];
        } else {
            let a : any = this.refs["tagsInput"];
            completions = this.state.availableTags.filter(function (availableTag) {
                var tagName = availableTag.name.toLowerCase();

                var matchesAvailableTag = tagName.substr(0, input.length) == input;
                var isAlreadyAdded = a.getTags().filter((existingTag) => tagEquals(existingTag, availableTag)).length > 0;

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
            var a : any = this.refs["tagsInput"];
            tagsActionCreator.addTag(this.props.note, tag);
            a.clearInput();
            a.clear();
        };
        let tagText = this.getTagNameWithParentInfo(tag);
        return <span onClick={addTag} className={this.tagStyles.tag} style={{cursor: "pointer"}}>{tagText}</span>;
    }

    private renderTag = (index: any, tag: Tag) => {
        let tagText = this.getTagNameWithParentInfo(tag);
        let removeTag = () => this.removeTag(tag);
        return (<span className={this.tagStyles.tag}>
            <span>{tagText}</span>
            <a className={this.tagStyles.remove} onClick={removeTag}></a>
        </span>);
    }

    private getTagNameWithParentInfo(tag: Tag) : string {
        let tagIterator = tag;
        let tagText : string = tag.name;
        while (tagIterator.parent) {
            tagText = tagIterator.parent.name + " > " + tagText;
            tagIterator = tagIterator.parent;
        }
        return tagText;
    }

    private render() : ReactElement<any> {
        var renderedTagCompletions = this.state.tagCompletions.map(this.renderTagCompletion);

        return (
            <div>
                <TagsInput
                    ref="tagsInput"
                    classNames={this.tagStyles}
                    value={this.props.note.tags}
                    onTagAdd={this.addTag}
                    onTagRemove={this.removeTag}
                    renderTag={this.renderTag}
                    transform={(id) => id}

                    // The following are for the typeahead support
                    addOnBlur={false}
                    onChangeInput={this.updateTypeaheadMatches}
                    onChange={this.clearTypeaheadMatches} />

                <div style={{marginTop: "5px"}}>{renderedTagCompletions}</div>
            </div>
        );
    }
}
