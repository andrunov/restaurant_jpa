package ru.agorbunov.restaurant.repository;

import ru.agorbunov.restaurant.model.MenuList;

import java.util.List;

/**
 * Interface for MenuList-repository
 */
public interface MenuListRepository extends BaseRepository<MenuList>{

    /*save menuList in database, restaurantId in parameters is Id
    *of restaurant to which the menuList is belong*/
    MenuList save(MenuList menuList, int restaurantId);

    /*get menuList from database by Id, restaurantId in parameters is Id
    *of restaurant to which the menuList is belong*/
    MenuList get(int id, int restaurantId);

    /*get menuList from database by Id with collection of dishes which the menuList is have,
    * restaurantId in parameters is Id of restaurant to which the menuList is belong*/
    MenuList getWithDishes(int id, int restaurantId);

    /*get all menuLists from database that belongs to restaurant with Id pass as parameter */
    List<MenuList> getByRestaurant(int restaurantId);

    /*get all menuLists from database that belongs to restaurant with Id pass as 1st parameter
    * and according to status pass as 2nd parameter*/
    List<MenuList> getByRestaurantAndEnabled(int restaurantId, boolean enabled);

    /*get menuList from database by dish Id, belongs to this menu list*/
    MenuList getByDish(int dishId);
}
