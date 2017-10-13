package ru.agorbunov.restaurant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.agorbunov.restaurant.model.Dish;
import ru.agorbunov.restaurant.repository.DishRepository;
import ru.agorbunov.restaurant.service.DishService;
import ru.agorbunov.restaurant.util.ValidationUtil;

import java.util.List;
import java.util.Map;

import static ru.agorbunov.restaurant.util.ValidationUtil.checkArrCompatibility;
import static ru.agorbunov.restaurant.util.ValidationUtil.checkNotFoundWithId;

/**
 * Class for exchange dish-entity data between web and repository layers
 */
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishRepository repository;

    /*save dish, menulistId in parameters is Id of menu list to which the dish is belong
    * check dish for not-null, and check that dish was found (dish must belongs to this menuList) */
    @Override
    public Dish save(Dish dish, int menuListId) {
        Assert.notNull(dish,"dish must not be null");
        ValidationUtil.checkEmpty(dish.getDescription(),"description");
        ValidationUtil.checkEmpty(dish.getPrice(),"price");
        return checkNotFoundWithId(repository.save(dish,menuListId),dish.getId());
    }

    /*delete dish by Id, check that dish was found */
    @Override
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id),id);
    }

    /*get all dishes*/
    @Override
    public List<Dish> getAll() {
        return repository.getAll();
    }

    /*get dish by Id, menulistId in parameters is Id of menu list to which the dish is belong,
    * check that dish was found (dish must belongs to this menuList) */
    @Override
    public Dish get(int id, int menuListId) {
        return checkNotFoundWithId(repository.get(id,menuListId),id);
    }

    /*get dish by Id with collection of orders which contains the dish,
    * menulistId in parameters is Id of menu list to which the dish is belong,
    * check that dish was found (dish must belongs to this menuList) */
    @Override
    public Dish getWithOrders(int id, int menulistId) {
        return checkNotFoundWithId(repository.getWithOrders(id,menulistId),id);
    }

    /*get all dishes that belongs to menuList with Id pass as parameter */
    @Override
    public List<Dish> getByMenuList(int menuListId) {
        return repository.getByMenuList(menuListId);
    }

    /*get all dishes that belongs to order with Id pass as parameter */
    @Override
    public Map<Dish,Integer> getByOrder(int orderId) {
        return repository.getByOrder(orderId);
    }

    /*delete dishes from order, dish Id and order Id pass in parameters */
    @Override
    public boolean deleteFromOrder(int id, int orderId) {
        return repository.deleteFromOrder(id,orderId);
    }
}
