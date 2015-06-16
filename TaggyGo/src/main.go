package main

import (
	"github.com/gin-gonic/gin"
	"webapp"
	"log"
	"os"
	"github.com/rcrowley/go-metrics"
)

func main() {
	env := os.Getenv("TAGGY_ENVIRONMENT")
	if env == "prod" {
		setupProductionEnvironment();
	} else {
		log.Println("[WARN] Starting Taggy in develoment environment")
		setupDevEnvironment()
	}

	ginEngine := gin.Default()
	ginEngine.Use(goMetricsMiddleWare)
	setupRoutes(ginEngine)
	startTheServer(ginEngine)
}

// Measures the time each request takes and how often each endpoint is accessed.
func goMetricsMiddleWare(c *gin.Context) {
	key := c.Request.Method + ";" + c.Request.URL.Path

	metrics.GetOrRegisterMeter("meter;" + key, metrics.DefaultRegistry).Mark(1)
	metrics.GetOrRegisterTimer("timer;" + key, metrics.DefaultRegistry).Time(c.Next)
}

func setupProductionEnvironment() {
	gin.SetMode(gin.ReleaseMode)
}

func setupDevEnvironment() {
	gin.SetMode(gin.DebugMode)
}

func setupRoutes(r *gin.Engine) {
	setupTagRoutes(r)
	setupNoteRoutes(r)

	r.GET("/metrics", func (c *gin.Context){
		metrics.WriteJSONOnce(metrics.DefaultRegistry, c.Writer)
	})
}

func setupTagRoutes(r *gin.Engine) {
	r.POST("/tags", webapp.AddTag)
}

func setupNoteRoutes(r *gin.Engine) {
	r.POST("/notes", webapp.AddNote)
	r.DELETE("/notes/:id", webapp.DeleteNote)
}

func startTheServer(r *gin.Engine) {
	log.Println("Starting the server")
	err := r.RunTLS(":8080", "keys/dev.crt", "keys/dev.key") // listen and serve https on 0.0.0.0:8080
	if err != nil {
		log.Fatalf("Unable to start the server. %v\n", err)
	}
}