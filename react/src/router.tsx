import {HashLocation, Route, RouterState, create} from 'react-router';
import {render, ComponentClass} from 'react';

import {routeActionCreator} from './actions/route-action-creator';

// Components
import {RootComponent} from './components/root';
var Root = RootComponent;

import {LoginComponent} from './components/login';
var Login = LoginComponent;

import {FiltersStateKeeper} from "./components/filters-state-keeper";
import {FilterMasterDetail} from "./components/filter-master-detail";

export function initRouter() {
  var options = {
    routes: (
      <Route name="root" path="/" handler={Root}>
        <Route path="/filters" handler={FiltersStateKeeper}>
            <Route name="filters" path="/filters" handler={FilterMasterDetail} />
            <Route path="/filters/:selectedId" handler={FilterMasterDetail} />
            <Route path="/filters/:selectedId/:selectedNote" handler={FilterMasterDetail} />
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
