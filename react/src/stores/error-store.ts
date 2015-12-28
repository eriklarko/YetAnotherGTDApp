import {Component, ReactElement} from 'react';
import {EventEmitter} from 'eventemitter';
import {dispatcher, Action} from '../dispatcher';

export class Error {

    static idCounter: number = 0;

    private id : number;
    constructor(public element: ReactElement<any>) {
        this.id = Error.idCounter++;
    }

    public getId() : number {
        return this.id;
    }
}

class ErrorStore {
  private changedEvent: string = "error-changed-event";
  private eventEmitter = new EventEmitter();

  private currentState : Array<Error> = [];

  constructor() {
    this.registerWithDispatcher();
  }

  private registerWithDispatcher(): void {
    dispatcher.register(this.handleActions);
  }

  private handleActions = (action : any) : void => {
    switch (action.type) {
      case "new-error":
        this.currentState.push(new Error(action.element));
        this.eventEmitter.emit(this.changedEvent);
        break;
      case "dismiss-error":
        this.remove(action.error);
        this.eventEmitter.emit(this.changedEvent);
        break;
    }
  }

    private remove(errorToRemove: Error) : void {
        let index = this.findIndexOfFirst(this.currentState, (error: Error) => {return error.getId() === errorToRemove.getId()});
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

  public getErrors() : Array<Error> {
      return this.currentState;
  }
}

export var errorStore : ErrorStore = new ErrorStore();
