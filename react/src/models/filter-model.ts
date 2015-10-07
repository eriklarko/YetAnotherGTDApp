export enum DisplayType {
    Cards, MasterDetail
}

export class Filter {
	name: string;
    searchTree: SearchTree;
    displayType: DisplayType;
}

export class SearchTree {
    raw: string;
}
