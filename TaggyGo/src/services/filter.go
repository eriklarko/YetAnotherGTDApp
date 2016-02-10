package services

import (
	"models"
	"fmt"
	"errors"
	"log"
)

var filters map[int]models.Filter = make(map[int]models.Filter);
var nextFilterId int = 1;

/**
 * Persists the filter and returns the filter with the Id set
 */
func AddFilter(filter *models.Filter) (*models.Filter, error) {
	filter.Id = nextFilterId;
	nextFilterId++
	filters[filter.Id] = *filter;

	fmt.Printf("Successfully added filter, id: %d, starred: '%s', searchTree: %v\n", filter.Id, filter.Starred, filter.SearchTree)
	return filter, nil
}

func DeleteFilter(filterId int) error {
	filter := filters[filterId];
	if filter.Id == 0 {
		return errors.New("no such filter")
	}

	delete(filters, filterId);
	log.Printf("Successfully removed filter with id %d\n", filterId)
	return nil
}

func GetAllFilters() []models.Filter {
	f := make([]models.Filter, 0, len(filters))
	for  _, value := range filters {
		f = append(f, value)
	}
	return f;
}