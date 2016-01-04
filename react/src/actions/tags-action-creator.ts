import {dispatcher} from '../dispatcher';
import {Note} from "../models/note-model";
import {Tag, tagEquals} from "../models/tag-model";

class TagsActionCreator {
    public addTag(note: Note, tag: Tag) : void {
        note.tags.push(tag);
            dispatcher.dispatch({
            type: "note-updated",
            note: note
        });
    }

    public removeTag(note: Note, tagToRemove: Tag) : void {
        let index = this.findIndexOfFirst(note.tags, (t: Tag) => tagEquals(tagToRemove, t));
        if (index > -1) {
            note.tags.splice(index, 1);
            dispatcher.dispatch({
                type: "note-updated",
                note: note
            });
        }
    }

    private findIndexOfFirst<T>(array: Array<T>, predicate: (T)=>boolean) : number {
        for (let i = 0; i < array.length; i++) {
            if (predicate(array[i])) {
                return i;
            }
        }
        return -1;
    }
}

export var tagsActionCreator : TagsActionCreator = new TagsActionCreator();
