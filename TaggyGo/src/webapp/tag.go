package webapp

import (
	"services"
	"misc"
	"models"
	"github.com/gin-gonic/gin"
)

func AddTag(c *gin.Context) {
	var tag models.Tag
	c.BindJSON(&tag)
	err, savedTag := services.AddTag(&tag)

	if err == nil {
		c.JSON(200, savedTag)
	} else {
		c.JSON(400, misc.JsonMessage{Message: err.Error()})
	}
}