import {Component, ReactElement} from 'react';
import {Link} from 'react-router';
import {Filter} from "../models/filter-model";
import {GenericMasterDetailView} from "./generic-master-detail";
import {FilterView} from "./filter-view";

interface State {
}

interface Props {
  filters : Array<Filter>;
  params: any;
}

export class FilterMasterDetail extends Component<Props, State>  {

  private render() : ReactElement<any> {
      return <GenericMasterDetailView
        items={this.props.filters}
        defaultView="select a filter, damnation"

        getId={(f) => f.name}

        selectedViewContructor={(f) => <FilterView filter={f} selectedNote={this.props.params.selectedNote}/>}
        linkConstructor={(f) => <div><Link to={"/filters/" + f.name}>{f.name}</Link></div>}

        params={this.props.params}
        />
  }
}
