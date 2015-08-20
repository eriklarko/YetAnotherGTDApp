import {Component, ReactElement} from 'react';
import {Link, RouterState, Route, RouteProp} from 'react-router';
import {routeStore} from '../stores/route-store';

interface MenuItem {
  routeName: string;
  title: string;
}

interface MenuState {
  menuItems : Array<Array<MenuItem>>;
  currentRouteState : RouterState;
}

interface MenuProps {
}

export class MenuComponent extends React.Component<MenuProps, MenuState> {
  constructor() {
    super();

    this.state = {
        menuItems: [
          [
            // Menu gropu 1
            {routeName: "dashboard", title: "Dashboard"},
          ]
        ],
        currentRouteState: routeStore.getCurrentState()
    };
  }

  private componentDidMount() : void {
    routeStore.addChangeListener(this.onChange);
  }

  private componentWillUnmount() : void {
    routeStore.removeChangeListener(this.onChange);
  }

  private onChange = () : void => {
    this.state.currentRouteState = routeStore.getCurrentState();
  }

  private isRouteActive(routeName: string) : boolean {
    if(this.state.currentRouteState != null) {
      for(var i in this.state.currentRouteState.routes) {
        // We need any here the definition is not correct.
        var route  : any = this.state.currentRouteState.routes[i];
        if(route.name == routeName) {
          return true;
        }
      }
      return false;
    }
  }

  private renderItem(item : MenuItem) : ReactElement<any> {
    var active : boolean = this.isRouteActive(item.routeName);
    return (
      <li className={active?"active":""} >
        <Link to={item.routeName}>
          {item.title} {(active? <span className="sr-only">(current)</span> : "")}
        </Link>
      </li>
    );
  }

  private renderSection(section : Array<MenuItem>) : ReactElement<any> {
    var content : ReactElement<any>[] = section.map((item : MenuItem) : ReactElement<any> => {
        return this.renderItem(item);
      });

      return (<ul className="nav nav-sidebar">{content}</ul>);
  }

  private render() : ReactElement<any> {
    var id = 0;
    var menu = this.state.menuItems.map((section) : ReactElement<any> => {
      return this.renderSection(section);
    });

    return (
      <div className="col-sm-3 col-md-2 sidebar">
        {menu}
      </div>
    );
  }

}
