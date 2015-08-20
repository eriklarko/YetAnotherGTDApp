import {HashLocation, Route, RouterState, create} from 'react-router';
import {render, ComponentClass} from 'react';

import {routeActionCreator} from './actions/route-action-creator';

// Components
import {RootComponent} from './components/root';
var Root = RootComponent;

import {ContentComponent} from './components/content';
var Content = ContentComponent;

import {DashboardComponent} from './components/dashboard';
var Dashboard = DashboardComponent;

import {LoginComponent} from './components/login';
var Login = LoginComponent;

export function initRouter() {
  var options = {
    routes: (
      <Route name="root" path="/" handler={Root}>
        <Route name="content" path="/" handler={Content}>
          <Route name="dashboard" path="/" handler={Dashboard} />
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
