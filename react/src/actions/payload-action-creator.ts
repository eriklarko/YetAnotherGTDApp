import {dispatcher} from '../dispatcher';
import {Note} from "../models/note-model";

class PayloadActionCreator {
  public updatePayload(note: Note, newPayload: string) : void {
	note.payload = newPayload;

    dispatcher.dispatch({
		type: "note-updated",
		note: note
	});
  }
}

export var payloadActionCreator : PayloadActionCreator = new PayloadActionCreator();
