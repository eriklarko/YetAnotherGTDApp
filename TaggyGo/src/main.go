package main

import (
	"github.com/gin-gonic/gin"
	"fmt"
	"net/http/httputil"
	"net/http"
	"models"
)

func main() {
	r := gin.Default()
	r.POST("/notes", func(c *gin.Context) {
		dump(c.Request)

		var note models.Note
		c.BindJSON(&note)
		fmt.Printf("Adding note, payload: '%s', tags: %v\n", note.Payload, note.Tags)

		c.String(200, "")
	})
	r.RunTLS(":8080", "keys/dev.crt", "keys/dev.key") // listen and serve on 0.0.0.0:8080
}

func dump(req *http.Request) {
	b, err := httputil.DumpRequest(req, true)
	var s string = string(b)

	fmt.Printf("Err %v\n", err);
	fmt.Printf("Dmp %v\n", s);
}