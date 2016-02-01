package webapp

import (
	"github.com/gin-gonic/gin"
	"github.com/graphql-go/graphql"
	"log"
	"models"
)

func GraphQLEntryPoint(c *gin.Context) {
	query := c.Query("query")
	result := executeQuery(query);
	if len(result.Errors) > 0 {
		log.Printf("Error executing graphql query: %v\n", result.Errors)
		c.String(500, "Error executing graphql query: %v\n", result.Errors)
	} else {
		c.JSON(200, result.Data);
	}
}

func executeQuery(query string) *graphql.Result {
	result := graphql.Do(graphql.Params{
		Schema:        Schema,
		RequestString: query,
	})
	return result
}

var Schema = buildSchema();

func buildSchema() graphql.Schema {
	// define schema, with our rootQuery and rootMutation
	var schema, err = graphql.NewSchema(graphql.SchemaConfig{
		Query:    rootQuery,
		Mutation: rootMutation,
	});

	if err != nil {
		log.Fatalf("Failed creating graphql schema. %v\n", err)
	}
	return schema;
}






var tagType = graphql.NewObject(graphql.ObjectConfig {
	Name: "Tag",
	Fields: graphql.Fields {
		"name": &graphql.Field {
			Type: graphql.String,
		},
	},
})

// define custom GraphQL ObjectType `todoType` for our Golang struct `Todo`
// Note that
// - the fields in our todoType maps with the json tags for the fields in our struct
// - the field type matches the field type in our struct
var noteType = graphql.NewObject(graphql.ObjectConfig {
	Name: "Note",
	Fields: graphql.Fields {
		"id": &graphql.Field {
			Type: graphql.Int,
		},
		"payload": &graphql.Field {
			Type: graphql.String,
		},
		"tags": &graphql.Field {
			Type: graphql.NewList(tagType),
		},
		"type": &graphql.Field {
			Type: graphql.Int,
		},
	},
})

// root mutation
var rootMutation = graphql.NewObject(graphql.ObjectConfig{
	Name: "RootMutation",
	Fields: graphql.Fields{
		/*
		   curl -g 'http://localhost:8080/graphql?query=mutation+_{createTodo(text:"My+new+todo"){id,text,done}}'
		*/
		"createCreate": &graphql.Field{
			Type: noteType, // the return type for this field
			Args: graphql.FieldConfigArgument{
				"payload": &graphql.ArgumentConfig{
					Type: graphql.NewNonNull(graphql.String),
				},
			},
			Resolve: func(params graphql.ResolveParams) (interface{}, error) {

				// marshall and cast the argument value
				payload, _ := params.Args["payload"].(string)

				// perform mutation operation here
				// for e.g. create a Todo and save to DB.
				newTodo := models.Note {
					Payload: payload,
					Tags: make([]string, 0),
					Id: 1,
				}

				// return the new Todo object that we supposedly save to DB
				// Note here that
				// - we are returning a `Todo` struct instance here
				// - we previously specified the return Type to be `todoType`
				// - `Todo` struct maps to `todoType`, as defined in `todoType` ObjectConfig`
				return newTodo, nil
			},
		},
	},
})

// root query
// we just define a trivial example here, depuis root query is required.
// Test with curl
// curl -g 'http://localhost:8080/graphql?query={lastTodo{id,payload}}'
var rootQuery = graphql.NewObject(graphql.ObjectConfig {
	Name: "RootQuery",
	Fields: graphql.Fields{

		/*
		   curl -g 'http://localhost:8080/graphql?query={todo(id:"b"){id,text,done}}'
		*/
		"todo": &graphql.Field{
			Type: noteType,
			Args: graphql.FieldConfigArgument{
				"id": &graphql.ArgumentConfig{
					Type: graphql.String,
				},
			},
			Resolve: func(params graphql.ResolveParams) (interface{}, error) {

				idQuery, isOK := params.Args["id"].(string)
				if isOK {
					log.Println("Looking for " + idQuery);
				}

				return models.Note{}, nil
			},
		},

		"lastTodo": &graphql.Field{
			Type:        noteType,
			Description: "Last todo added",
			Resolve: func(params graphql.ResolveParams) (interface{}, error) {
				return models.Note{}, nil
			},
		},

		/*
		   curl -g 'http://localhost:8080/graphql?query={todoList{id,text,done}}'
		*/
		"todoList": &graphql.Field{
			Type:        graphql.NewList(noteType),
			Description: "List of todos",
			Resolve: func(p graphql.ResolveParams) (interface{}, error) {
				return make([]models.Note, 0), nil
			},
		},
	},
})






