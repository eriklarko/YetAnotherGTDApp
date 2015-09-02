import {Tag} from "./tag-model";

export class Note {
  id : number;
  payload : string;
  tags: Array<Tag>;
}
