package services

import (
	"models"
	"fmt"
	"math/rand"
)

/**
 * Persists the note and returns the note with the Id set
 */
func AddNote(note *models.Note) (error, *models.Note) {
	fmt.Printf("Adding note, id: %d, payload: '%s', tags: %v\n", note.Id, note.Payload, note.Tags)
	note.Id = int(rand.Int31())
	return nil, note
}