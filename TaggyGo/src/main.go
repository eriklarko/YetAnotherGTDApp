package main

import (
	"github.com/gin-gonic/gin"
	"fmt"
	"net/http/httputil"
	"net/http"
	"models"
	"services"
)

func main() {
	r := gin.Default()
	r.POST("/notes", addNote)
	r.RunTLS(":8080", "keys/dev.crt", "keys/dev.key") // listen and serve on 0.0.0.0:8080
}

func addNote(c *gin.Context) {
	var note models.Note
	c.BindJSON(&note)
	note = *services.AddNote(&note)

	c.JSON(200, note)
}

func dump(req *http.Request) {
	b, err := httputil.DumpRequest(req, true)
	var s string = string(b)

	fmt.Printf("Err %v\n", err);
	fmt.Printf("Dmp %v\n", s);
}