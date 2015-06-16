package webapp

import (
	"github.com/gin-gonic/gin"
	"models"
	"services"
	"misc"
)

func AddNote(c *gin.Context) {
	var note models.Note
	c.BindJSON(&note)
	err, savedNote := services.AddNote(&note)

	if err == nil {
		c.JSON(200, savedNote)
	} else {
		c.JSON(400, misc.JsonError{Message: err.Error()})
	}
}