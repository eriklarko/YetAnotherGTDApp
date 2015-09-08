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
import {Note, NoteType} from "./models/note-model";
var notes : Array<Note> = [{
	id: 1,
	payload: "HEJSAN",
	tags: [{name: "tag1"}, {name: "tag2"}],
	type: NoteType.Short
},{
	id: 2,
	payload: "# hej\
	\n`apa`",
	tags: [{name: "123"}, {name: "567"}],
	type: NoteType.MarkedUpText
}];
dispatcher.dispatch({type: "notes", notes: notes});
