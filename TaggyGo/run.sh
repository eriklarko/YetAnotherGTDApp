. ./setup-gopath.sh
go vet src/main.go
$GOPATH/bin/golint src/main.go
go run src/main.go
