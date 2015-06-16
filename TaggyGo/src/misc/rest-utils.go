package misc
import (
	"net/http"
	"net/http/httputil"
	"fmt"
)

type JsonError struct {
	Message string
}



func Dump(req *http.Request) {
	b, err := httputil.DumpRequest(req, true)
	var s string = string(b)

	fmt.Printf("Err %v\n", err);
	fmt.Printf("Dmp %v\n", s);
}
