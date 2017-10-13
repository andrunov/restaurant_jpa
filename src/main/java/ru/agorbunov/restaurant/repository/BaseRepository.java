package ru.agorbunov.restaurant.repository;

import java.util.List;

/**
 * Base interface. This methods must have all repositories
 */
public interface BaseRepository<T> {

    /*delete entity from database by Id */
    boolean delete(int id);

    /*get all entities from database*/
    List<T> getAll();

}
