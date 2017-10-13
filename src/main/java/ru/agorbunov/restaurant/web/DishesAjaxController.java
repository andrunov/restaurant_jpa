package ru.agorbunov.restaurant.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.agorbunov.restaurant.model.Dish;
import ru.agorbunov.restaurant.model.MenuList;
import ru.agorbunov.restaurant.model.Restaurant;
import ru.agorbunov.restaurant.service.DishService;
import ru.agorbunov.restaurant.service.MenuListService;
import ru.agorbunov.restaurant.util.ValidationUtil;

import java.util.List;

/**
 * Rest controller for works with dishes.jsp
 * to exchange dish data with dishService-layer
 */
@RestController
@RequestMapping(value =  "/ajax/dishes")
public class DishesAjaxController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DishService dishService;

    @Autowired
    private MenuListService menuListService;

    /*get dishes by current menu list*/
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Dish> getByMenuList() {
        log.info("getByMenuList");
        MenuList currentMenuList = CurrentEntities.getCurrentMenuList();
        return dishService.getByMenuList(currentMenuList.getId());
    }

    /*get dishes by menu list Id pass as parameter*/
    @GetMapping(value = "byMenuList/{menuListId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Dish> getByMenuListId(@PathVariable("menuListId") int menuListId) {
        log.info("getByMenuList");
        Restaurant currentRestaurant = CurrentEntities.getCurrentRestaurant();
        MenuList currentMenuList = menuListService.get(menuListId,currentRestaurant.getId());
        CurrentEntities.setCurrentMenuList(currentMenuList);
        return dishService.getByMenuList(menuListId);
    }

    /*get dish by Id and current menu list*/
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Dish getDish(@PathVariable("id") int id) {
        log.info("get " + id);
        MenuList MenuList = CurrentEntities.getCurrentMenuList();
        return dishService.get(id,MenuList.getId());
    }

    /*delete dish by Id*/
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") int id) {
        log.info("delete " + id);
        dishService.delete(id);
    }

    /*create new dish or update if exist */
    @PostMapping
    public void createOrUpdate(@RequestParam("id") Integer id,
                               @RequestParam("description") String description,
                               @RequestParam("price") Double price){
        MenuList menuList = CurrentEntities.getCurrentMenuList();
        Dish dish = new Dish(description,price);
        dish.setId(id);
        if (dish.isNew()) {
            ValidationUtil.checkNew(dish);
            log.info("create " + dish);
            dishService.save(dish,menuList.getId());
        } else {
            log.info("update " + dish);
            dishService.save(dish,menuList.getId());
        }
    }
}

