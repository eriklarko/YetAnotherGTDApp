import {dispatcher} from '../dispatcher';
import {RouterState} from 'react-router';

class RouteActionCreator {
  public updateRoute(newState : RouterState) : void {
    dispatcher.dispatch({type: "route-state-update", "state": newState})
  }
}

export var routeActionCreator : RouteActionCreator = new RouteActionCreator();
