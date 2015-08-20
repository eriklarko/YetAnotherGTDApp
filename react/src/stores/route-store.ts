import {EventEmitter} from 'eventemitter';
import {dispatcher, Action} from '../dispatcher';
import {RouterState} from 'react-router';

interface RouteAction extends Action {
  state : RouterState;
}

class RouteStore {
  private changedEvent: string = "route-changed-event";
  private eventEmitter = new EventEmitter();

  private currentState : any;

  constructor() {
    this.registerWithDispatcher();
  }

  private registerWithDispatcher(): void {
    dispatcher.register(this.handleActions);
  }

  private handleActions = (action : RouteAction) : void => {
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

  public getCurrentState() : RouterState {
    return this.currentState;
  }
}

export var routeStore : RouteStore = new RouteStore();
