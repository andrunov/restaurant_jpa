package ru.agorbunov.restaurant.service;

import ru.agorbunov.restaurant.model.MenuList;

import java.util.List;

/**
 * Interface for MenuList-service
 */
public interface MenuListService extends BaseService<MenuList> {

    /*save menuList, restaurantId in parameters is Id
    *of restaurant to which the menuList is belong*/
    MenuList save(MenuList menuList, int restaurantId);

    /*get menuList by Id, restaurantId in parameters is Id
    *of restaurant to which the menuList is belong*/
    MenuList get(int id, int restaurantId);

    /*get menuList by Id with collection of dishes which the menuList is have,
    * restaurantId in parameters is Id of restaurant to which the menuList is belong*/
    MenuList getWithDishes(int id, int restaurantId);

    /*get all menuLists that belongs to restaurant with Id pass as parameter */
    List<MenuList> getByRestaurant(int restaurantId);

    /*get all menuLists that belongs to restaurant with Id pass as 1st parameter
    * and according to status pass as 2nd parameter*/
    List<MenuList> getByRestaurantAndEnabled(int restaurantId, boolean enabled);

    /*get menuList by dish Id, belongs to this menu list*/
    MenuList getByDish(int dishId);
}
