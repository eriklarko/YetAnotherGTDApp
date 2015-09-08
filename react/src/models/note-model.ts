import {Tag} from "./tag-model";

export enum NoteType {
    Short, MarkedUpText, Audio, Video
}

export class Note {
  id : number;
  payload : string; // Should probably be a byte[] later so that audio and video can be supported
  tags: Array<Tag>;
  type: NoteType;
}
