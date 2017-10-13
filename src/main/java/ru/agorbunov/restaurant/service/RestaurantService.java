package ru.agorbunov.restaurant.service;

import ru.agorbunov.restaurant.model.Restaurant;

/**
 * Interface for Restaurant-service
 */
public interface RestaurantService extends BaseService<Restaurant> {

    /*save restaurant*/
    Restaurant save(Restaurant restaurant);

    /*get restaurant by Id*/
    Restaurant get(int id);

    /*get restaurant by Id with collection of
    *menuLists were issued by the restaurant*/
    Restaurant getWithMenuLists(int id);

    /*evict service-layer cash of restaurant-entities*/
    void evictCache();

}
