import {Component, ReactElement} from 'react';

interface State<T> {
	selectedItem: T;
}

interface Props<T> {
  items : Array<T>;
  getId : (T) => any;
  params : {selectedId: any};
  defaultView: ReactElement<any>;
  selectedViewContructor: (T) => ReactElement<any>;
  linkConstructor: (T) => ReactElement<any>;
}

export class GenericMasterDetailView<T> extends Component<Props<T>, State<T>> {

  constructor() {
	super();
	this.state = {selectedItem: null}
  }

  private componentWillReceiveProps(newProps: Props<T>) {
    if (newProps.params === undefined) {
        console.log("Props did not contain any params", newProps);
        return;
    }

	this.setState({
        selectedItem: this.findFirst(
            newProps.items,
            (t: T) => this.props.getId(t) === newProps.params.selectedId
        )
    });
  }

  private findFirst<T>(array : Array<T>, predicate: (T)=>boolean) : T {
	for (let a of array) {
		if (predicate(a)) {
			return a;
		}
	}

	return null;
  }

  private summarize(obj) {
	return obj.summary || obj.payload;
  }

  private render() : ReactElement<any> {

    let selectedItemView = null;
	if (this.state.selectedItem == null) {
		selectedItemView = this.props.defaultView;
	} else {
        selectedItemView = this.props.selectedViewContructor(this.state.selectedItem);
    }

	let list = this.props.items.map(this.props.linkConstructor);
    return (
      <div style={{height: "100%"}}>
	    <div className="col-md-1">{list}</div>
        <div className="col-md-11" style={{height: "100%"}}>{selectedItemView}</div>
      </div>
    );
  }
}
