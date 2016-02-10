package services

import (
	"models"
	"errors"
	"log"
)

var notes map[int]models.Note = make(map[int]models.Note);
var nextNoteId int = 1;

/**
 * Persists the note and returns the note with the Id set
 */
func AddNote(note *models.Note) (*models.Note, error) {
	note.Id = nextNoteId
	nextNoteId++
	notes[note.Id] = *note;

	log.Printf("Successfully added note, id: %d, payload: '%s', tags: %v\n", note.Id, note.Payload, note.Tags)
	return note, nil
}

func DeleteNote(noteId int) error {
	note := notes[noteId];
	if note.Id == 0 {
		return errors.New("no such note")
	}

	delete(notes, noteId);
	log.Printf("Successfully removed note with id %d\n", noteId)
	return nil
}

func GetAllNotes() []models.Note {
	n := make([]models.Note, 0, len(notes))
	for  _, value := range notes {
		n = append(n, value)
	}
	return n;
}