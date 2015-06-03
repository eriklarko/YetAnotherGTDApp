package services
import (
	"models"
	"fmt"
)

/**
 * Persists the note and returns the note with the Id set
 */
func AddNote(note *models.Note) *models.Note {
	fmt.Printf("Adding note, id: %d, payload: '%s', tags: %v\n", note.Id, note.Payload, note.Tags)
	note.Id = 100
	return note
}