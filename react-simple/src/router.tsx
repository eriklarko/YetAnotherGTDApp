import * as React from "react";
import * as ReactDOM from "react-dom";
import { Router, Route, hashHistory } from "react-router";

// Components
import {RootComponent} from './components/root';
  
/*import {LoginComponent} from './components/login';
    
import {FiltersStateKeeper} from "./components/filter/filters-state-keeper";
import {FilterMasterDetail} from "./components/filter/filter-master-detail";

import {FilterViewController} from "./components/filter/filter-view-controller";
import {FilterEditController} from "./components/filter/filter-edit-controller";
  
import {NoteReminderViewController} from "./components/note-reminders/note-reminder-view-controller";
  
import {Search} from "./search";
import {FilterMenu} from "./filter/filter-menu";
var AllFiltersList;
*/
export var route = (<Router history={hashHistory}>
    <Route path="/" component={RootComponent}>
{/*        <Route path="/" handler={FiltersStateKeeper}>
            <Route path="/" handler={FilterMenu}>

                <Route path="/filters/:filterName" handler={FilterViewController} />
                <Route path="/filters/:filterName/:selectedNote" handler={FilterViewController} />
                <Route path="/filters/all" handler={AllFiltersList} />

                <Route name="search" path="/search/:query" handler={Search} />

                <Route name="filter" path="/filter/:filterName"         handler={FilterViewController} />
                <Route               path="/filter/:filterName/:noteId" handler={FilterViewController} />

                <Route path="/editfilter/:filterId" handler={FilterEditController} />
                <Route name="note" path="/note/:id" handler={NoteReminderViewController} />
                <Route name="nr" path="nr" handler={NoteReminderViewController} />
            </Route>
        </Route>

        <Route name="login" path="login" handler={LoginComponent}/>
*/}
    </Route>
</Router>);
