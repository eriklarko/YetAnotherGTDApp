import {dispatcher} from '../dispatcher';
import {Note} from "../models/note-model";

class TagsActionCreator {
  public addTag(note: Note, tag: string) : void {
	note.tags.push(tag);
    dispatcher.dispatch({
		type: "note-updated",
		note: note
	});
  }

  public removeTag(note: Note, tag: string) : void {
	var index = note.tags.indexOf(tag);
	if (index > -1) {
	    note.tags.splice(index, 1);
		dispatcher.dispatch({
			type: "note-updated",
			note: note
		});
	}
  }
}

export var tagsActionCreator : TagsActionCreator = new TagsActionCreator();
