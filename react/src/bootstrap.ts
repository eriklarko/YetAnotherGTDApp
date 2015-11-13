// React needs to be in the global scope
import * as React from 'react';
declare var window : any;
window.React = React;

// Load bootstrap, the ui framework
import 'bootstrap';

// Load all stores
import {noteStore} from "./stores/note-store";
import {routeStore} from "./stores/route-store";
import {filterStore} from "./stores/filter-store";
var KEEP = {
	noteStore,
	routeStore,
    filterStore
};

// Run the application
import {initRouter} from './router';
initRouter();






// Seed with dummy data
import {dispatcher} from "./dispatcher";
import {Note, NoteType} from "./models/note-model";
import {Filter, DisplayType} from "./models/filter-model";

var numNotes = 100;

var notes : Array<Note> = [];
for (let i = 0; i < numNotes; i++) {
    var tag1 = {name: "tag1", parent: {name: "a"}};
    notes.push({
		id: i,
		payload: stringGen(Math.floor(Math.random() * 20)),
		tags: [tag1, {name: "tag2"}, {name: "neo", parent: tag1}],
		type: NoteType.Short
	});
}

let filter = {
	name: "Master detail",
	searchTree: {raw: ""},
    displayType: DisplayType.MasterDetail,
    starred: true
};
dispatcher.dispatch({type: "new-filter", filter: filter});
filter = {
	name: "Cards",
	searchTree: {raw: ""},
    displayType: DisplayType.Cards,
    starred: true
};
dispatcher.dispatch({type: "new-filter", filter: filter});

function stringGen(len) {
    var text = "";
    var charset = "aeiouyl";
    for( var i=0; i < len; i++ ) {
        text += charset.charAt(Math.floor(Math.random() * charset.length));
    }
    return text;
}

dispatcher.dispatch({type: "notes", notes: notes});

dispatcher.dispatch({type: "note-reminder", noteReminder: {
    whenToRemind: new Date(),
    noteToRemindAbout: {
		id: 1,
		payload: stringGen(Math.floor(Math.random() * 20)),
		tags: [tag1, {name: "tag2"}, {name: "neo"}],
		type: NoteType.Short
	}
}});
dispatcher.dispatch({type: "note-reminder", noteReminder: {
    whenToRemind: new Date(12321312312333),
    noteToRemindAbout: {
		id: 1,
		payload: stringGen(Math.floor(Math.random() * 20)),
		tags: [tag1, {name: "tag2"}, {name: "neo"}],
		type: NoteType.Short
	}
}});
