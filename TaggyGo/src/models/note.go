package models

type Note struct {
	Payload string `json:payload binding:required`
	tags []string `json:tags binding:required`
}