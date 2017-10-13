package ru.agorbunov.restaurant.service;

import ru.agorbunov.restaurant.model.User;
import ru.agorbunov.restaurant.util.exception.NotFoundException;

/**
 * Interface for User-service
 */
public interface UserService extends BaseService<User> {

    /*save user*/
    User save(User user);

    /*get user by Id*/
    User get(int id);

    /*get user by Id with collection of
    *orders were made by the user*/
    User getWithOrders(int id);

    /*get user by email*/
    User getByEmail(String email) throws NotFoundException;


    /*evict service-layer cash of user-entities*/
    void evictCache();
}
