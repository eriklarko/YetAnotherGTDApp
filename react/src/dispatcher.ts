import {Dispatcher} from 'flux';

export interface Action {
  type: string;
  [actionSpecificArguments: string] : any;
}

export var dispatcher : Dispatcher<Action> = new Dispatcher<Action>();
