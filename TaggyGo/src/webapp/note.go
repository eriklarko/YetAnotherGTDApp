package webapp

import (
	"github.com/gin-gonic/gin"
	"models"
	"services"
	"misc"
	"strconv"
	"fmt"
)

func AddNote(c *gin.Context) {
	var note models.Note
	c.BindJSON(&note)
	savedNote, err := services.AddNote(&note)

	if err == nil {
		c.JSON(200, savedNote)
	} else {
		c.JSON(400, misc.JsonMessage{Message: err.Error()})
	}
}

func DeleteNote(c *gin.Context) {
	rawNoteId := c.Param("id")
	noteId, err := strconv.Atoi(rawNoteId)
	if err != nil {
		c.JSON(422, misc.JsonMessage{Message: fmt.Sprintf("Invalid id '%s', %v", rawNoteId, err.Error())})
	}

	err = services.DeleteNote(noteId)
	if err == nil {
		c.JSON(200, misc.JsonMessage{Message: "ok"})
	} else { // TODO: This assumes that the only possible error returned from services.DeleteNote means that the note did not exist
		c.JSON(422, misc.JsonMessage{Message: err.Error()})
	}
}