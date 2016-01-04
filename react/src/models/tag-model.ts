export interface Tag {
	name: string;
    parent?: Tag;
}

export function tagEquals(a: Tag, b: Tag) : boolean {
    if (a.parent && b.parent) { // Both parents are non-null, not undefined, not the empty string etc...
        return a.name === b.name && tagEquals(a.parent, b.parent);
    } else if (a.parent == b.parent) { // At least one of the parents are null or undefined or something,
                                       // if they are equal now, both must be nullish and we can compare by name
        return a.name === b.name;
    } else { // One parent is nullish, the other is not. These are not the same tags.
        return false;
    }
}
