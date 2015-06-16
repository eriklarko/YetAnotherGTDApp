package main

import (
	"github.com/gin-gonic/gin"
	"fmt"
	"net/http/httputil"
	"net/http"
	"models"
	"services"
	"misc"
)

func main() {
	r := gin.Default()

	r.POST("/tags", addTag)
	r.POST("/notes", addNote)

	r.RunTLS(":8080", "keys/dev.crt", "keys/dev.key") // listen and serve https on 0.0.0.0:8080
}

func addTag(c *gin.Context) {
	var tag models.Tag
	c.BindJSON(&tag)
	err, savedTag := services.AddTag(&tag)

	if err == nil {
		c.JSON(200, savedTag)
	} else {
		c.JSON(400, misc.JsonError{Message: err.Error()})
	}
}

func addNote(c *gin.Context) {
	var note models.Note
	c.BindJSON(&note)
	err, savedNote := services.AddNote(&note)

	if err == nil {

		c.JSON(200, savedNote)
	} else {
		c.JSON(400, misc.JsonError{Message: err.Error()})
	}
}

func dump(req *http.Request) {
	b, err := httputil.DumpRequest(req, true)
	var s string = string(b)

	fmt.Printf("Err %v\n", err);
	fmt.Printf("Dmp %v\n", s);
}