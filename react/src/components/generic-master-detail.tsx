import {Component, ReactElement} from 'react';

interface State {
}

interface Props<T> {
    items : Array<T>;
    getId : (T) => any;
    selectedId: any;
    defaultView: ReactElement<any>;
    selectedViewContructor: (t: T) => ReactElement<any>;
    linkConstructor: (t: T, selected: boolean) => ReactElement<any>;

    listDetailRenderer: (list: Array<ReactElement<any>>, detail: ReactElement<any>) => ReactElement<any>;
}

// This is a stupid idea... :) The views using this are hard to understand and debug.
export class GenericMasterDetailView<T> extends Component<Props<T>, State> {

    private findSelectedItem() : T {
        if (this.props.selectedId) {
            let item = this.findFirst(
                this.props.items,
                (t: T) => this.props.getId(t) === this.props.selectedId
            )

            if (item != undefined) {
                return item;
            }
        }

         if (this.props.items.length > 0){
            console.debug("Defaulted to first item in collection");
            return this.props.items[0];
        } else {
            return undefined;
        }
    }

    private findFirst<T>(array : Array<T>, predicate: (T)=>boolean) : T {
        for (let a of array) {
            if (predicate(a)) {
                return a;
            }
        }

        return undefined;
    }

    private horizontalRenderer(list: Array<ReactElement<any>>, detail: ReactElement<any>) : ReactElement<any> {
        return (
            <div style={{height: "100%"}}>
                <div className="col-md-1">{list}</div>
                <div className="col-md-11" style={{height: "100%"}}>{detail}</div>
            </div>
        );
    }

    private render() : ReactElement<any> {
        let selectedItem = this.findSelectedItem();
        let selectedItemView = null;
        if (selectedItem) {
            selectedItemView = this.props.selectedViewContructor(selectedItem);
        } else {
            selectedItemView = this.props.defaultView;
        }

        let list : Array<ReactElement<any>> = this.props.items.map(
            (item: T) => {
                let selected = item === selectedItem;
                return this.props.linkConstructor(item, selected);
            });
        let renderer = this.props.listDetailRenderer || this.horizontalRenderer;
        return renderer(list, selectedItemView);
    }
}
