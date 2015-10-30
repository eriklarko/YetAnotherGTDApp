import {EventEmitter} from 'eventemitter';
import {dispatcher, Action} from '../dispatcher';
import {NoteReminder} from "../models/note-reminder";

class NoteReminderStore {

    private changedEvent: string = "note-reminder-changed-event";
    private eventEmitter = new EventEmitter();

    private currentState : Array<NoteReminder> = [];

    constructor() {
        this.registerWithDispatcher();
    }

    private registerWithDispatcher(): void {
        dispatcher.register(this.handleActions);
    }

    private handleActions = (action : any) : void => {
      switch (action.type) {
        case "note-reminder":
            this.currentState.push(action.noteReminder);
            this.eventEmitter.emit(this.changedEvent);
            break;
        case "dismiss-note-reminder":
            this.remove(action.noteReminder);
            this.eventEmitter.emit(this.changedEvent);
            break;
      }
    }

    private remove(noteReminder: NoteReminder) : void {
        let index = this.findIndexOfFirst(this.currentState, (nr: NoteReminder) => {return nr == noteReminder});
        if (index > -1) {
           this.currentState.splice(index, 1);
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

    public addChangeListener(listener) {
        this.eventEmitter.addListener(this.changedEvent, listener);
    }

    public removeChangeListener(listener) {
        this.eventEmitter.removeListener(this.changedEvent, listener);
    }

    public getCurrentState() : Array<NoteReminder> {
        return this.currentState;
    }
}

export var noteReminderStore : NoteReminderStore = new NoteReminderStore();
