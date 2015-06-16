package main

import (
	"github.com/gin-gonic/gin"
	"webapp"
)

func main() {
	r := gin.Default()

	r.POST("/tags", webapp.AddTag)
	r.POST("/notes", webapp.AddNote)

	r.RunTLS(":8080", "keys/dev.crt", "keys/dev.key") // listen and serve https on 0.0.0.0:8080
}