package ru.agorbunov.restaurant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.agorbunov.restaurant.model.MenuList;
import ru.agorbunov.restaurant.repository.MenuListRepository;
import ru.agorbunov.restaurant.service.MenuListService;

import java.util.List;

import static ru.agorbunov.restaurant.util.ValidationUtil.checkNotFoundWithId;

/**
 * Class for exchange menuList-entity data between web and repository layers
 */
@Service
public class MenuListServiceImpl implements MenuListService {

    @Autowired
    private MenuListRepository repository;

    /*save menuList, restaurantId in parameters is Id of restaurant to which the menuList is belong
    * check menu list for not null, and check that menuList was found (menuList belongs for this restaurant) */
    @Override
    public MenuList save(MenuList menuList, int restaurantId) {
        Assert.notNull(menuList,"menu list must not be null");
        return checkNotFoundWithId(repository.save(menuList,restaurantId),menuList.getId());
    }

    /*delete menuLists by Id, and check that menuList was found */
    @Override
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id),id);
    }

    /*get all menuLists*/
    @Override
    public List<MenuList> getAll() {
        return repository.getAll();
    }

    /*get menuList by Id, restaurantId in parameters is Id
    *of restaurant to which the menuList is belong,
    * check that menuList was found (menuList belongs for this restaurant) */
    @Override
    public MenuList get(int id, int restaurantId) {
        return checkNotFoundWithId(repository.get(id, restaurantId),id);
    }

    /*get menuList by Id with collection of dishes which the menuList is have,
    * restaurantId in parameters is Id of restaurant to which the menuList is belong,
    * check that menuList was found (menuList belongs for this restaurant) */
    @Override
    public MenuList getWithDishes(int id, int restaurantId) {
        return checkNotFoundWithId(repository.getWithDishes(id, restaurantId),id);
    }

    /*get all menuLists that belongs to restaurant with Id pass as parameter */
    @Override
    public List<MenuList> getByRestaurant(int restaurantId) {
        return repository.getByRestaurant(restaurantId);
    }

    /*get all menuLists that belongs to restaurant with Id pass as 1st parameter
    * and according to status pass as 2nd parameter*/
    @Override
    public List<MenuList> getByRestaurantAndEnabled(int restaurantId, boolean enabled) {
        return repository.getByRestaurantAndEnabled(restaurantId, enabled);
    }

    /*get menuList by dish Id, belongs to this menu list*/
    @Override
    public MenuList getByDish(int dishId) {
        return checkNotFoundWithId(repository.getByDish(dishId),dishId);
    }
}
