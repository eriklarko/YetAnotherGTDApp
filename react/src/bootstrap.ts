// React needs to be in the global scope
import * as React from 'react';
declare var window : any;
window.React = React;

// Load bootstrap, the ui framework
import 'bootstrap';

// Load all stores
import {noteStore} from "./stores/note-store";
import {routeStore} from "./stores/route-store";
var KEEP = {
	noteStore,
	routeStore
};

// Run the application
import {initRouter} from './router';
initRouter();






// Seed with dummy data
import {dispatcher} from "./dispatcher";
import {Note} from "./models/note-model";
var notes : Array<Note> = [{
	id: 1,
	payload: "HEJSAN",
	tags: ["tag1", "tag2"]
},{
	id: 2,
	payload: "asdsad",
	tags: ["123", "567"]
}];
dispatcher.dispatch({type: "notes", notes: notes});
