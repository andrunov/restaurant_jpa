package ru.agorbunov.restaurant.service;

import java.util.List;

/**
 * Base interface. This methods must have all services
 */
public interface BaseService<T> {

    /*delete entity by Id */
    void delete(int id);

    /*get all entities*/
    List<T> getAll();

}
