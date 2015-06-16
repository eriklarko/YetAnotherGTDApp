package main

import (
	"github.com/gin-gonic/gin"
	"webapp"
)

func main() {
	r := gin.Default()
	setupRoutes(r)

	r.RunTLS(":8080", "keys/dev.crt", "keys/dev.key") // listen and serve https on 0.0.0.0:8080
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