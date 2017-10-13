package ru.agorbunov.restaurant.service;

import ru.agorbunov.restaurant.model.Dish;

import java.util.List;
import java.util.Map;

/**
 * Interface for Dish-service
 */
public interface DishService extends BaseService<Dish> {

    /*save dish, menulistId in parameters is Id of menu list to which the dish is belong*/
    Dish save(Dish dish,  int menulistId);

    /*get dish by Id, menulistId in parameters is Id of menu list to which the dish is belong*/
    Dish get(int id, int menulistId);

    /*get dish by Id with collection of orders which contains the dish,
    * menulistId in parameters is Id of menu list to which the dish is belong*/
    Dish getWithOrders(int id, int menulistId);

    /*get all dishes that belongs to menuList with Id pass as parameter */
    List<Dish> getByMenuList(int menuListId);

    /*get all dishes that belongs to order with Id pass as parameter */
    Map<Dish, Integer> getByOrder(int orderId);

    /*delete dishes from order, dish Id and order Id pass in parameters */
    boolean deleteFromOrder(int id,int orderId);

}
