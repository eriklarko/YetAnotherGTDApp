import {Component, ReactElement} from 'react';
import {RouteHandler} from 'react-router';

import {MenuComponent} from './menu';
import {CardGrid} from "./note-views/card-grid";
import {MasterDetailView} from "./note-views/master-detail";

import {Filter} from "../models/filter-model";
import {filterStore} from "../stores/filter-store";

interface State {
    filters: Array<Filter>
}

interface Props {
}

// This class should not exist :) I need to move the stores down into the components that need them.
export class FiltersStateKeeper extends Component<Props, State> {

  constructor() {
      super();
      this.state = {filters: filterStore.getFilters()};
  }

  private componentWillMount() : void {
    filterStore.addChangeListener(this.onChange);
  }

  private componentWillUnmount() : void {
    filterStore.removeChangeListener(this.onChange);
  }

  private onChange = () : void => {
    this.setState({filters: filterStore.getFilters()});
  }

  private render() : ReactElement<any> {
    return <RouteHandler filters={this.state.filters}/>
  }
}
