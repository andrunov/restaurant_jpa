package ru.agorbunov.restaurant.repository;

import ru.agorbunov.restaurant.model.Dish;

import java.util.List;
import java.util.Map;

/**
 * Interface for Dish-repository
 */
public interface DishRepository extends BaseRepository<Dish> {

    /*save dish in database, menulistId in parameters is Id of menu list to which the dish is belong*/
    Dish save(Dish dish,  int menulistId);

    /*get dish from database by Id, menulistId in parameters is Id of menu list to which the dish is belong*/
    Dish get(int id, int menuListId);

    /*get dish from database by Id with collection of orders which contains the dish,
    * menulistId in parameters is Id of menu list to which the dish is belong*/
    Dish getWithOrders(int id, int menuListId);

    /*get all dishes from database that belongs to menuList with Id pass as parameter */
    List<Dish> getByMenuList(int menuListId);

    /*get all dishes from database that belongs to order with Id pass as parameter */
    Map<Dish, Integer> getByOrder(int orderId);

    /*delete references to dishes from order in database, dish Id and order Id pass in parameters */
    boolean deleteFromOrder(int id,int orderId);
}
