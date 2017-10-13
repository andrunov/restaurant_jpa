package ru.agorbunov.restaurant.repository;

import ru.agorbunov.restaurant.model.Restaurant;

/**
 * Interface for Restaurant-repository
 */
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    /*save restaurant in database*/
    Restaurant save(Restaurant restaurant);

    /*get restaurant from database by Id*/
    Restaurant get(int id);

    /*get restaurant from database by Id with collection of
    *menuLists were issued by the restaurant*/
    Restaurant getWithMenuLists(int id);
}
