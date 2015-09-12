import {dispatcher} from '../dispatcher';
import {EventEmitter} from 'eventemitter';

import {Note} from '../models/note-model';

export enum LoadingState {NotLoaded, Loading, Loaded, Error};
class NoteStore {
  private changedEvent: string = "notes-changed-event";
  private eventEmitter : EventEmitter = new EventEmitter();

  public loadingState : LoadingState = LoadingState.NotLoaded;
  public errorMessage : string;
  public notes : Array<Note> = [];

  constructor() {
    this.registerWithDispatcher();
  }

  private registerWithDispatcher(): void {
    dispatcher.register(this.handleActions);
  }

  private handleActions = (action : any) : void => {
    switch (action.type) {
      case "notes-requested":
        this.loadingState = LoadingState.Loading;
        this.eventEmitter.emit(this.changedEvent);
        break;
      case "notes":
        this.loadingState = LoadingState.Loaded;
        this.notes = action.notes;
        this.eventEmitter.emit(this.changedEvent);
        break;
      case "notes-error":
        this.loadingState = LoadingState.Error;
        this.errorMessage = action.text;
        this.eventEmitter.emit(this.changedEvent);
        break;
      case "note-updated":
        this.replaceNote(action.note);
        break;
    }
  }

  private replaceNote(note: Note): void {
      if (this.notes === undefined || this.notes === null) {
          this.notes = [note];
          this.eventEmitter.emit(this.changedEvent);
          return;
      }

      for (var i in this.notes) {
          if (this.notes[i].id === note.id) {
              this.notes[i] = note;
              this.eventEmitter.emit(this.changedEvent);
              return;
          }
      }

      console.log("[WARNING] Tried to replace a note that did not exist in the store", note, this.notes);
  }

  public addChangeListener(listener) : void {
    this.eventEmitter.addListener(this.changedEvent, listener);
  }

  public removeChangeListener(listener) : void {
    this.eventEmitter.removeListener(this.changedEvent, listener);
  }

  public getNotes() : Array<Note> {
    return this.notes;
  }
}

export var noteStore : NoteStore = new NoteStore();