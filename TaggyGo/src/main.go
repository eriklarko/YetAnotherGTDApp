package main

import (
	"github.com/gin-gonic/gin"
	"webapp"
	"log"
)

func main() {
	r := gin.Default()
	setupRoutes(r)

	log.Println("Starting the server")
	err := r.RunTLS(":8080", "keys/dev.crt", "keys/dev.key") // listen and serve https on 0.0.0.0:8080
	if err != nil {
		log.Fatalf("Unable to start the server. %v\n", err)
	}
}

func setupRoutes(r *gin.Engine) {
	setupTagRoutes(r)
	setupNoteRoutes(r)
}

func setupTagRoutes(r *gin.Engine) {
	r.POST("/tags", webapp.AddTag)
}

func setupNoteRoutes(r *gin.Engine) {
	r.POST("/notes", webapp.AddNote)
}