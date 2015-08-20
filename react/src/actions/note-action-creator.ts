import {dispatcher} from '../dispatcher';

import * as jquery from "jquery";

declare var window : any;

class NotesActionCreator {

  public requestAllNotes() : void {
    dispatcher.dispatch({type: "notes-requested"});

    var host : string = window.location.hostname;
    var url : string = "http://".concat(host,":5001","/api/v1/Note/summaries/");

    jquery.getJSON(url)
      .done(function (response) {
        setTimeout(() => {
          if(Math.random()>0.5) {
            dispatcher.dispatch({
                type: "notes",
                notes: response
            });
          }
          else {
            dispatcher.dispatch({
                type: "notes-error",
                text: "404 page not found",
                source: "requestNotes",
            });
          }
        }, Math.random()*4000);
      })
      .fail(function( jqxhr, textStatus, error ) {
        dispatcher.dispatch({
            type: "notes-error",
            text: textStatus + ", " + error,
            source: "requestNotes",
        });
      });
  }
}

export var notesActionCreator : NotesActionCreator = new NotesActionCreator();
