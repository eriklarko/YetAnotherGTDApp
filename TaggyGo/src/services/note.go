package services

import (
	"models"
	"fmt"
	"math/rand"
	"errors"
)

/**
 * Persists the note and returns the note with the Id set
 */
func AddNote(note *models.Note) (*models.Note, error) {
	fmt.Printf("Adding note, id: %d, payload: '%s', tags: %v\n", note.Id, note.Payload, note.Tags)
	note.Id = int(rand.Int31())
	return note, nil
}

func DeleteNote(noteId int) error {
	return errors.New("no such note")
}