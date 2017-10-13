package ru.agorbunov.restaurant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.agorbunov.restaurant.model.Restaurant;
import ru.agorbunov.restaurant.repository.RestaurantRepository;
import ru.agorbunov.restaurant.service.RestaurantService;
import ru.agorbunov.restaurant.util.ValidationUtil;

import java.util.List;

import static ru.agorbunov.restaurant.util.ValidationUtil.checkNotFoundWithId;

/**
 * Class for exchange restaurant-entity data between web and repository layers
 */
@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository repository;

    /*save restaurant, check that restaurant not null*/
    @CacheEvict(value = "restaurants", allEntries = true)
    @Override
    public Restaurant save(Restaurant restaurant) {
        Assert.notNull(restaurant,"restaurant must not be null");
        ValidationUtil.checkEmpty(restaurant.getName(),"name");
        ValidationUtil.checkEmpty(restaurant.getAddress(),"address");
        return repository.save(restaurant);
    }

    /*delete restaurant by Id, check that restaurant was found */
    @CacheEvict(value = "restaurants", allEntries = true)
    @Override
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id),id);
    }

    /*get all restaurants*/
    @Cacheable("restaurants")
    @Override
    public List<Restaurant> getAll() {
        return repository.getAll();
    }

    /*get restaurant by Id, check that restaurant was found*/
    @Override
    public Restaurant get(int id) {
        return checkNotFoundWithId(repository.get(id),id);
    }

    /*get restaurant by Id with collection of menuLists were issued by the restaurant,
    * check that restaurant was found */
    @Override
    public Restaurant getWithMenuLists(int id) {
        return checkNotFoundWithId(repository.getWithMenuLists(id),id);
    }

    /*evict service-layer cash of restaurant-entities*/
    @CacheEvict(value = "restaurants", allEntries = true)
    @Override
    public void evictCache() {

    }
}
