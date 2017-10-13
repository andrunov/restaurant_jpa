package ru.agorbunov.restaurant.repository;

import ru.agorbunov.restaurant.model.Order;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface for Order-repository
 */
public interface OrderRepository extends BaseRepository<Order> {

    /*save order in database if it is new entity and update if it is exist,
    *int[] dishIds - Ids of dishes, int[] dishQuantityValues - dishes quantities,
    *each dishId from first arr matches its quantity from second arr, arrays must have equal size
    *userId and restaurantId in parameters is Ids of user and restaurant to which the order is belong*/
    Order save(Order order,  int userId, int restaurantId, int[] dishIds, int[] dishQuantityValues);

    /*save order in database if it is new entity and update if it is exist,
    *userId and restaurantId in parameters is Ids of user and restaurant to which the order is belong,
    *if order is already exist and have collections of dishes they not erase in database*/
    Order save(Order order,  int userId, int restaurantId);

    /*get order from database by Id, userId and restaurantId in parameters is Ids of
    *user and restaurant to which the order is belong*/
    Order get(int id, int userId, int restaurantId);

    /*get order from database by Id with collections of dishes which the order is have ,
    *userId and restaurantId in parameters is Ids of
    *user and restaurant to which the order is belong*/
    Order getWithDishes(int id, int userId, int restaurantId);

    /*get all orders from database that belongs to user with Id pass as parameter */
    List<Order> getByUser(int userId);

    /*get all orders from database that belongs to user with Id pass as 1st parameter
     * and with status pass as 2nd parameter */
    List<Order> getByUserAndStatus(int userId, String status);

    /*get all orders from database that belongs to user with Id pass as 1st parameter
     * and which made on Date  pass as 2nd parameter */
    List<Order> getByUserAndDate(int userId, LocalDateTime localDateTime);

    /*get all orders from database that belongs to user with Id pass as 1st parameter
    * and with status pass as 2nd parameter and which made on Date  pass as 3rd parameter */
    List<Order> getByUserAndStatusAndDate(int userId, String status,LocalDateTime localDateTime);

    /*get all orders from database that belongs to dish with Id pass as parameter */
    List<Order> getByDish(int dishId);

    /*get all orders from database that belongs to dish with Id pass as parameter *
     * and with status pass as 2nd parameter */
    List<Order> getByDishAndStatus(int dishId, String status);

    /*get all orders from database that belongs to dish with Id pass as parameter *
     * and which made on Date  pass as 2nd parameter */
    List<Order> getByDishAndDate(int dishId, LocalDateTime localDateTime);

    /*get all orders from database that belongs to dish with Id pass as parameter *
    * and with status pass as 2nd parameter and which made on Date  pass as 3rd parameter */
    List<Order> getByDishAndStatusAndDate(int dishId, String status, LocalDateTime localDateTime);
}
