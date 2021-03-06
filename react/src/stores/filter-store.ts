import {EventEmitter} from 'eventemitter';
import {dispatcher, Action} from '../dispatcher';
import {Filter} from "../models/filter-model";

class FilterStore {
  private changedEvent: string = "filter-changed-event";
  private eventEmitter = new EventEmitter();

  private currentState : Array<Filter> = [];

  constructor() {
    this.registerWithDispatcher();
  }

  private registerWithDispatcher(): void {
    dispatcher.register(this.handleActions);
  }

  private handleActions = (action : any) : void => {
    switch (action.type) {
        case "new-filter":
          this.currentState.push(action.filter);
          this.eventEmitter.emit(this.changedEvent);
          break;
        case "removed-filter":
          this.remove(this.currentState, action.filter);
          this.eventEmitter.emit(this.changedEvent);
          break;
        case "filter-updated":
          this.replaceFilter(action.filter);
          this.eventEmitter.emit(this.changedEvent);
          break;
    }
  }

  private replaceFilter(filter: Filter): void {
      if (this.currentState === undefined || this.currentState === null) {
          this.currentState = [filter];
          this.eventEmitter.emit(this.changedEvent);
          return;
      }

      for (var i in this.currentState) {
          if (this.currentState[i].id === filter.id) {
              this.currentState[i] = filter;
              this.eventEmitter.emit(this.changedEvent);
              return;
          }
      }

      console.log("[WARNING] Tried to replace a filter that did not exist in the store", filter, this.currentState);
  }

  private remove(array: Array<Filter>, filter: Filter) {
      var index = this.findIndexOf(array, (f) => f.id, filter.id);
      array.splice(index, 1);
  }

  private findIndexOf<T,S>(array: Array<T>, getId: (T)=>S, id: S) : number {
      return this.findIndexOfFirst(array, (t) => getId(t) === id);
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

  public getFilters() : Array<Filter> {
      return this.currentState;
  }
}

export var filterStore : FilterStore = new FilterStore();
