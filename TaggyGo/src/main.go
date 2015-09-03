package main

import (
	"github.com/gin-gonic/gin"
	"webapp"
	"log"
	"os"
	"github.com/rcrowley/go-metrics"
	"strconv"
)

var port uint16 = 8080
var certificateFile string
var keyFile string

func main() {
	env := os.Getenv("TAGGY_ENVIRONMENT")
	if env == "prod" {
		setupProductionEnvironment();
	} else {
		log.Println("[WARN] Starting Taggy in develoment environment. Start it in production with 'export TAGGY_ENVIRONMENT=prod'")
		setupDevEnvironment()
	}

	ginEngine := gin.Default()
	ginEngine.Use(goMetricsMiddleWare)
	// Todo: Make sure the metrics middleware is the first middleware to be executed to get more accurate timings
	setupRoutes(ginEngine)
	startTheServerAndBlock(ginEngine)
}

// Measures the time each request takes and how often each endpoint is accessed.
func goMetricsMiddleWare(c *gin.Context) {
	key := c.Request.Method + ";" + c.Request.URL.Path

	metrics.GetOrRegisterMeter("meter;" + key, metrics.DefaultRegistry).Mark(1)
	metrics.GetOrRegisterTimer("timer;" + key, metrics.DefaultRegistry).Time(c.Next)
}

func setupProductionEnvironment() {
	gin.SetMode(gin.ReleaseMode)
	certificateFile = os.Getenv("TAGGY_CERTFILE")
	keyFile = os.Getenv("TAGGY_KEYFILE")

	if len(certificateFile) == 0 || len(keyFile) == 0 {
		log.Fatalln("You must set TAGGY_CERTFILE and TAGGY_KEYFILE environment variables to point to your HTTPS certificate files")
	}

	readPortFromEnvIfItsSet()
}

func setupDevEnvironment() {
	gin.SetMode(gin.DebugMode)
	certificateFile = "keys/dev.crt"
	keyFile = "keys/dev.key"
	readPortFromEnvIfItsSet()
}

func readPortFromEnvIfItsSet() {
	rawPort := os.Getenv("TAGGY_PORT")
	if len(rawPort) == 0 {
		return
	}

	uncheckedPort, err := strconv.Atoi(rawPort)
	if err != nil {
		log.Fatalf("Unknown port set by environment variable TAGGY_PORT: '%s'\n", rawPort)
	} else if uncheckedPort >= 0 && uncheckedPort < 65536 {
		port = uint16(uncheckedPort)
	} else {
		log.Fatalf("Illegal port set by environment variable TAGGY_PORT: %d. Must be >= 0 and < 65536\n", uncheckedPort)
	}
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

func startTheServerAndBlock(r *gin.Engine) {
	portAsString := strconv.Itoa(int(port))

	log.Printf("Starting the server on port %s using %s and %s\n", portAsString, certificateFile, keyFile)
	err := r.RunTLS(":" + portAsString, certificateFile, keyFile) // listen and serve https on localhost
	if err != nil {
		log.Fatalf("Unable to start the server. %v\n", err)
	}
}