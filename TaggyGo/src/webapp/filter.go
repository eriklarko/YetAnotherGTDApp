package webapp

import (
	"github.com/gin-gonic/gin"
	"models"
	"services"
	"misc"
	"strconv"
	"fmt"
)

func GetAllFilters(c *gin.Context) {
	c.JSON(200, services.GetAllFilters())
}

func AddFilter(c *gin.Context) {
	var filter models.Filter
	c.BindJSON(&filter)
	savedFilter, err := services.AddFilter(&filter)

	if err == nil {
		c.JSON(200, savedFilter)
	} else {
		c.JSON(400, misc.JsonMessage{Message: err.Error()})
	}
}

func DeleteFilter(c *gin.Context) {
	rawFilterId := c.Param("id")
	filterId, err := strconv.Atoi(rawFilterId)
	if err != nil {
		c.JSON(422, misc.JsonMessage{Message: fmt.Sprintf("Invalid filter id '%s', %v", rawFilterId, err.Error())})
	}

	err = services.DeleteFilter(filterId)
	if err == nil {
		c.JSON(200, misc.JsonMessage{Message: fmt.Sprintf("filter %d deleted", filterId)})
	} else { // TODO: This assumes that the only possible error returned from services.DeleteFilter means that the filter did not exist
		c.JSON(422, misc.JsonMessage{Message: err.Error()})
	}
}