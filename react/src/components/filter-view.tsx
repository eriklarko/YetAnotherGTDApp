import {Component} from 'react';
import {Filter} from "../models/filter-model";

interface State {
}

interface Props {
  filter : Filter;
}

export class FilterView extends Component<Props, State>  {

  private render() {
      return <div>{this.props.filter.name}</div>
  }
}
