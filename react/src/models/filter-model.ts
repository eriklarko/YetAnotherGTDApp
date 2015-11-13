export enum DisplayType {
    Cards, MasterDetail
}

export class Filter {
	name: string;
    searchTree: SearchTree;
    displayType: DisplayType;
    starred: boolean;
}

export class SearchTree {
    raw: string;
}
