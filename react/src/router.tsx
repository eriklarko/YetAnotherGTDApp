import {HashLocation, Route, RouterState, create} from 'react-router';
import {render, ComponentClass} from 'react';

import {routeActionCreator} from './actions/route-action-creator';

// Components
import {RootComponent} from './components/root';
var Root = RootComponent;

import {LoginComponent} from './components/login';
var Login = LoginComponent;

import {NotesStateKeeper} from './components/notes-state-keeper';
import {MasterDetailView} from './components/note-views/master-detail';
import {CardGrid} from './components/note-views/card-grid';

import {FiltersStateKeeper} from "./components/filters-state-keeper";
import {FilterMasterDetail} from "./components/filter-master-detail";

export function initRouter() {
  var options = {
    routes: (
      <Route name="root" path="/" handler={Root}>
        <Route path="/notes" handler={NotesStateKeeper}>
            <Route name="richNotesDefault" path="richNote" handler={MasterDetailView} />
            <Route name="richNotes" path="richNote/:noteId" handler={MasterDetailView} />
            <Route name="cards" path="cards" handler={CardGrid} />
        </Route>

        <Route path="/filters" handler={FiltersStateKeeper}>
            <Route name="filters" path="/filters" handler={FilterMasterDetail} />
            <Route path="/filters/:selectedId" handler={FilterMasterDetail} />
        </Route>
        <Route name="login" path="login" handler={Login}/>
      </Route>
      ),
    location: HashLocation
  };

  create(options).run((Root : ComponentClass<any>, state : RouterState) => {
    routeActionCreator.updateRoute(state);
    render(<Root />, document.getElementById('content'));
  });
}
