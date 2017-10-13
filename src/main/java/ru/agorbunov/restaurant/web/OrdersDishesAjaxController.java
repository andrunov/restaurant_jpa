package ru.agorbunov.restaurant.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.agorbunov.restaurant.model.MenuList;
import ru.agorbunov.restaurant.model.Order;
import ru.agorbunov.restaurant.service.DishService;
import ru.agorbunov.restaurant.service.MenuListService;
import ru.agorbunov.restaurant.to.DishChoiceTo;
import ru.agorbunov.restaurant.to.DishTo;
import ru.agorbunov.restaurant.util.DateTimeUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Rest controller for orders_dishes.jsp
 * to exchange dish data with dishService-layer
 */
@RestController
@RequestMapping(value = "/ajax/orders_dishes")
public class OrdersDishesAjaxController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    /*title of actual menu list - description % dateTime*/
    private String menuListTitle;

    /*dishes list of actual menu list*/
    private List<DishChoiceTo> actualMenuListDishes;

    @Autowired
    private DishService dishService;

    @Autowired
    private MenuListService menuListService;

    /*get dishes of current order*/
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DishTo> getByOrder() {
        log.info("getByOrder");
        Order order = CurrentEntities.getCurrentOrder();
        return DishTo.toList(dishService.getByOrder(order.getId()));
    }

    /*delete dish by Id and current order*/
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") int id) {
        log.info("delete " + id);
        int orderId = CurrentEntities.getCurrentOrder().getId();
        dishService.deleteFromOrder(id,orderId);
    }

    /*get dishes ids from client and find actual menu list*/
    @PostMapping(value = "dishesIds",produces = MediaType.APPLICATION_JSON_VALUE)
    public void setDishesIds(@RequestParam("dishIds")String[] dishIds) {
        log.info("post dishesIds");
        int[] intDishesIds = Arrays.stream(dishIds).mapToInt(Integer::parseInt).toArray();
        MenuList menuList = menuListService.getByDish(intDishesIds[0]);
        actualMenuListDishes = DishChoiceTo.createDishChoiceToList(menuList.getDishList(),intDishesIds);
        menuListTitle = menuList.getDescription() + ", " + DateTimeUtil.toString(menuList.getDateTime());
    }

    /*get dishes of menuList by dish Id, belongs to that menu list pass as parameter*/
    @GetMapping(value = "allDishesOfMenuList",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DishChoiceTo> getAllDishesOfMenuList() {
        log.info("get allDishesOfMenuList");
        return actualMenuListDishes;
    }

    /*get current menuList description and dateTime*/
    @GetMapping(value = "currentMenuList",produces = "text/plain;charset=UTF-8")
    public String getCurrentMenuList() {
        log.info("currentMenuList");
        return menuListTitle;
    }

}
