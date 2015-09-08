import {HashLocation, Route, RouterState, create} from 'react-router';
import {render, ComponentClass} from 'react';

import {routeActionCreator} from './actions/route-action-creator';

// Components
import {RootComponent} from './components/root';
var Root = RootComponent;

import {LoginComponent} from './components/login';
var Login = LoginComponent;

import {StateKeeper} from './components/state-keeper';
import {MasterDetailView} from './components/note-views/master-detail';

var Home = MasterDetailView;

export function initRouter() {
  var options = {
    routes: (
      <Route name="root" path="/" handler={Root}>
        <Route path="/" handler={StateKeeper}>
            <Route name="home" path="/" handler={Home} />
            <Route path="richNote/:noteId" handler={MasterDetailView} />
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
