package models

type Filter struct {
	Id int `json:"id" binding:optional`
	Name string `json:"name" binding:optional`
	Starred bool `json:"starred" binding:required`
	SearchTree string `json:"searchTree"`
}