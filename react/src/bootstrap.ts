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
var notes : Array<Note> = [];
for (let i = 0; i < 1; i++) {
    notes.push({
		id: i,
		payload: stringGen(Math.floor(Math.random() * 400)) + i,
		tags: [{name: "tag1"}, {name: "tag2"}],
		type: NoteType.Short
	});
}
function stringGen(len)
{
    var text = " ";

    var charset = "abcdefghijklmnopqrstuvwxyz0123456789 ";

    for( var i=0; i < len; i++ )
        text += charset.charAt(Math.floor(Math.random() * charset.length));

    return text;
}

dispatcher.dispatch({type: "notes", notes: notes});
