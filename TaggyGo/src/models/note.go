package models

type Note struct {
	Id int `json:"id" binding:optional`
	Payload string `json:"payload" binding:required`
	Tags []string `json:"tags" binding:required`
}