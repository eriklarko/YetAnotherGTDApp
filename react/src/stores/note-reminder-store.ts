import {EventEmitter} from 'eventemitter';
import {dispatcher, Action} from '../dispatcher';
import {NoteReminder} from "../models/note-reminder";

class NoteReminderStore {

    private changedEvent: string = "note-reminder-changed-event";
    private eventEmitter = new EventEmitter();

    private currentState : Array<NoteReminder>;

    constructor() {
        this.registerWithDispatcher();
    }

    private registerWithDispatcher(): void {
        dispatcher.register(this.handleActions);
    }

    private handleActions = (action : any) : void => {
      switch (action.type) {
        case "route-state-update":
            this.currentState = action.state;
            this.eventEmitter.emit(this.changedEvent);
            break;
      }
    }

    public addChangeListener(listener) {
        this.eventEmitter.addListener(this.changedEvent, listener);
    }

    public removeChangeListener(listener) {
        this.eventEmitter.removeListener(this.changedEvent, listener);
    }

    public getCurrentState() : any {
        return this.currentState;
    }
}

export var noteReminderStore : NoteReminderStore = new NoteReminderStore();
