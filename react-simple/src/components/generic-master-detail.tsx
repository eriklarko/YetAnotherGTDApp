import * as React from "react";

interface State {
}

interface Props<T> {
    items : Array<T>;
    getId : (t: T) => any;
    selectedId: any;
    defaultView: React.ReactElement<any> | string;
    selectedViewContructor: (t: T) => React.ReactElement<any>;
    linkConstructor: (t: T, selected: boolean) => React.ReactElement<any>;

    listDetailRenderer: (list: Array<React.ReactElement<any>>, detail: React.ReactElement<any>) => React.ReactElement<any>;
}

// This is a stupid idea... :) The views using this are hard to understand and debug.
export class GenericMasterDetailView<T> extends React.Component<Props<T>, State> {

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

    private findFirst<T>(array : Array<T>, predicate: (t:T)=>boolean) : T {
        for (let a of array) {
            if (predicate(a)) {
                return a;
            }
        }

        return undefined;
    }

    private horizontalRenderer(list: Array<React.ReactElement<any>>, detail: React.ReactElement<any>) : React.ReactElement<any> {
        return (
            <div style={{height: "100%"}}>
                <div className="col-md-1">{list}</div>
                <div className="col-md-11" style={{height: "100%"}}>{detail}</div>
            </div>
        );
    }

    render() : React.ReactElement<any> {
        let selectedItem = this.findSelectedItem();
        let selectedItemView : any = null;
        if (selectedItem) {
            selectedItemView = this.props.selectedViewContructor(selectedItem);
        } else {
            selectedItemView = this.props.defaultView;
        }

        let list : Array<React.ReactElement<any>> = this.props.items.map(
            (item: T) => {
                let selected = item === selectedItem;
                return this.props.linkConstructor(item, selected);
            });
        let renderer = this.props.listDetailRenderer || this.horizontalRenderer;
        return renderer(list, selectedItemView);
    }
}
