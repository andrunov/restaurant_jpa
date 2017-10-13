package ru.agorbunov.restaurant.repository;

import ru.agorbunov.restaurant.model.User;

/**
 * Interface for User-repository
 */
public interface UserRepository extends BaseRepository<User> {

    /*save user in database*/
    User save(User user);

    /*get user from database by Id*/
    User get(int id);

    /*get user from database by email*/
    User getByEmail(String email);

    /*get user from database by Id with collection of
    *orders were made by the user*/
    User getWithOrders(int id);

    /*get total amount of user's orders from database
    * and save it to database to users's field "totalOrdersAmount" */
    void accountAndSaveTotalOrdersAmount(int id);
}
