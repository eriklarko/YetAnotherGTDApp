import {noteReminderActionCreator} from "../../actions/note-reminder-action-creator";
import {Component, ReactElement} from 'react';

import {NoteReminder} from "../../models/note-reminder";
import {noteReminderStore} from "../../stores/note-reminder-store";
import {NoteReminderList} from "./note-reminder-list";

interface Props {
    params: any;
}

interface State {
    noteReminders: Array<NoteReminder>
}

export class NoteReminderViewController extends Component<Props, State> {

    constructor() {
        super();
        this.state = {noteReminders: noteReminderStore.getCurrentState()};
    }

    private componentWillMount() : void {
        noteReminderStore.addChangeListener(this.onChange);
    }

    private componentWillUnmount() : void {
        noteReminderStore.removeChangeListener(this.onChange);
    }

    private onChange = () : void => {
        this.setState({noteReminders: noteReminderStore.getCurrentState()});
    }

    private onDismiss = (noteReminder : NoteReminder) : void => {
        noteReminderActionCreator.dismiss(noteReminder);
    }

    private render() : ReactElement<any> {
        return <NoteReminderList
            noteReminders={this.state.noteReminders.filter(nr => nr.whenToRemind < new Date())}
            onDismiss={this.onDismiss}/>
    }
}
