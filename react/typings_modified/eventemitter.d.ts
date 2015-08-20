// https://github.com/yaakaito/EventEmitter.d.ts
// https://github.com/Olical/EventEmitter
declare module "eventemitter" {
    export class EventEmitter {

      getListeners(evt: string): Function[];
      getListeners(evt: RegExp): Function[];
      flattenListeners(listeners: Object[]): Function[];
      getListenersAsObject(evt: string): Object;
      getListenersAsObject(evt: RegExp): Object;
      addListener(evt: string, listener: Function): EventEmitter;
      addListener(evt: RegExp, listener: Function): EventEmitter;
      on(evt: string, listener: Function): EventEmitter;
      on(evt: RegExp, listener: Function): EventEmitter;
      addOnceListener(evt: string, listener: Function): EventEmitter;
      addOnceListener(evt: RegExp, listener: Function): EventEmitter;
      once(evt: string, listener: Function): EventEmitter;
      once(evt: RegExp, listener: Function): EventEmitter;
      defineEvent(evt: string): EventEmitter;
      removeListener(evt: string, listener: Function): EventEmitter;
      removeListener(evt: RegExp, listener: Function): EventEmitter;
      off(evt: string, listener: Function): EventEmitter;
      off(evt: RegExp, listener: Function): EventEmitter;
      addListeners(evt: string, listeners: Function[]): EventEmitter;
      addListeners(evt: RegExp, listeners: Function[]): EventEmitter;
      addListeners(evt: Object, listeners: Function[]): EventEmitter;
      removeListeners(evt: string, listeners: Function[]): EventEmitter;
      removeListeners(evt: RegExp, listeners: Function[]): EventEmitter;
      removeListeners(evt: Object, listeners: Function[]): EventEmitter;
      manipulateListeners(remove: boolean, evt: string, listeners: Function[]): EventEmitter;
      manipulateListeners(remove: boolean, evt: RegExp, listeners: Function[]): EventEmitter;
      manipulateListeners(remove: boolean, evt: Object, listeners: Function[]): EventEmitter;
      removeEvent(evt: string): EventEmitter;
      removeEvent(evt: RegExp): EventEmitter;
      emitEvent(evt: string, args: any[]): EventEmitter;
      emitEvent(evt: RegExp, args: any[]): EventEmitter;
      trigger(evt: string, args: any[]): EventEmitter;
      trigger(evt: RegExp, args: any[]): EventEmitter;
      emit(evt: string, ...args: any[]): EventEmitter;
      emit(evt: RegExp, ...args: any[]): EventEmitter;
      setOnceReturnValue(value: any): EventEmitter;
  }
}
