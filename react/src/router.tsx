import {HashLocation, Route, RouterState, create} from 'react-router';
import {render, ComponentClass} from 'react';

import {routeActionCreator} from './actions/route-action-creator';

// Components
import {RootComponent} from './components/root';
var Root = RootComponent;

import {LoginComponent} from './components/login';
var Login = LoginComponent;

import {FiltersStateKeeper} from "./components/filter/filters-state-keeper";
import {FilterMasterDetail} from "./components/filter/filter-master-detail";

import {FilterViewController} from "./components/filter/filter-view-controller";
import {FilterEditController} from "./components/filter/filter-edit-controller";

import {NoteReminderViewController} from "./components/note-reminders/note-reminder-view-controller";

import {Search} from "./components/search";
import {FilterMenu} from "./components/filter/filter-menu";
var AllFiltersList;

export function initRouter() {
  var options = {
    routes: (
      <Route name="root" path="/" handler={Root}>
        <Route path="/" handler={FiltersStateKeeper}>
            <Route path="/" handler={FilterMenu}>

                <Route path="/filters/:selectedId" handler={FilterViewController} />
                <Route path="/filters/:selectedId/:selectedNote" handler={FilterViewController} />
                <Route path="/filters/all" handler={AllFiltersList} />

                <Route name="search" path="/search/:query" handler={Search} />

                <Route name="filter" path="/filter/:filterId"         handler={FilterViewController} />
                <Route               path="/filter/:filterId/:noteId" handler={FilterViewController} />

                <Route path="/editfilter/:filterId"    handler={FilterEditController} />

                <Route name="note" path="/note/:id" handler={NoteReminderViewController} />
                <Route name="nr" path="nr" handler={NoteReminderViewController} />
            </Route>
        </Route>

        <Route name="login" path="login" handler={Login}/>
      </Route>
      )
  };

  create(options).run((Root : ComponentClass<any>, state : RouterState) => {
    routeActionCreator.updateRoute(state);
    render(<Root />, document.getElementById('content'));
  });
}
