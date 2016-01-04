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

export function hash(tag: Tag) : number {
    if (tag.parent) {
        return hashString(tag.name + hash(tag.parent));
    } else {
        return hashString(tag.name);
    }

}

function hashString(string : string) : number {
  var hash = 0, i, chr, len;
  if (string.length === 0) return hash;
  for (i = 0, len = string.length; i < len; i++) {
    chr   = string.charCodeAt(i);
    hash  = ((hash << 5) - hash) + chr;
    hash |= 0; // Convert to 32bit integer
  }
  return hash;
};
