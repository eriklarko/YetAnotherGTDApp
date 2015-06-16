package services

import (
	"models"
	"fmt"
	"strings"
	"errors"
)

/**
 * Persists the tag and then returns it
 */
func AddTag(tag *models.Tag) (error, *models.Tag) {
	if len(strings.TrimSpace(tag.Name)) == 0 {
		return errors.New("Tried to add tag with null or empty name"), nil
	}

	fmt.Printf("Adding tag '%s'\n", tag.Name)
	return nil, tag
}