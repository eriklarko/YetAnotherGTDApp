package main

import "github.com/gin-gonic/gin"

func main() {
	r := gin.Default()
	r.GET("/ping", func(c *gin.Context) {
		c.String(200, "pong")
	})
	r.RunTLS(":8080", "keys/dev.crt", "keys/dev.key") // listen and serve on 0.0.0.0:8080
}