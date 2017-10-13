package ru.agorbunov.restaurant.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.agorbunov.restaurant.model.*;
import ru.agorbunov.restaurant.service.OrderService;
import ru.agorbunov.restaurant.service.UserService;
import ru.agorbunov.restaurant.util.DateTimeUtil;
import ru.agorbunov.restaurant.util.ValidationUtil;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Rest controller for order_by_dish.jsp
 * to exchange order data with service-layer
 */
@RestController
@RequestMapping(value = "/ajax/order_by_dish")
public class OrderByDishAjaxController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    /*get all orders by current dish according filters values statusKey and date*/
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Order> getByDish(@RequestParam(value = "statusKey",required = false) String statusKey,
                                 @RequestParam(value = "dateKey",required = false)String dateKey) {
        log.info("getByDish");
        Dish currentDish = CurrentEntities.getCurrentDish();
        List<Order> result = null;
        if ((statusKey != null)&&(!statusKey.equals("ALL"))&&(ValidationUtil.checkEmpty(dateKey))){
            LocalDateTime dateTime = DateTimeUtil.parseLocalDate(dateKey).atStartOfDay();
            result = orderService.getByDishAndStatusAndDate(currentDish.getId(),statusKey,dateTime);
        }else if ((statusKey != null)&&(!statusKey.equals("ALL"))){
            result = orderService.getByDishAndStatus(currentDish.getId(),statusKey);
        }else if (ValidationUtil.checkEmpty(dateKey)){
            LocalDateTime dateTime = DateTimeUtil.parseLocalDate(dateKey).atStartOfDay();
            result = orderService.getByDishAndDate(currentDish.getId(),dateTime);
        }else {
            result = orderService.getByDish(currentDish.getId());
        }
        return result;
    }

    /*get order by Id of user by userId and of current restaurant */
    @GetMapping(value = "/{id}&{userId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Order getOrder(@PathVariable("id") int id,
                          @PathVariable("userId") int userId){
        log.info("get " + id);
        User user = userService.get(userId);
        CurrentEntities.setCurrentUser(user);
        Restaurant restaurant = CurrentEntities.getCurrentRestaurant();
        Order order = orderService.get(id, user.getId(),restaurant.getId());
        CurrentEntities.setCurrentOrder(order);
        return order;
    }

    /*update order, updates only DateTime and Status, dishes not touch*/
    @PostMapping
    public void update(@RequestParam("dateTime")@DateTimeFormat(pattern = DateTimeUtil.DATE_TIME_PATTERN) LocalDateTime dateTime,
                       @RequestParam("status") String status){
        User user = CurrentEntities.getCurrentUser();
        Restaurant restaurant = CurrentEntities.getCurrentRestaurant();
        Order order = CurrentEntities.getCurrentOrder();
        order.setDateTime(dateTime);
        order.setStatus(Status.valueOf(status));
        log.info("update " + order);
        orderService.save(order,user.getId(),restaurant.getId());
    }

    /*delete order by Id*/
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") int id) {
        log.info("delete " + id);
        orderService.delete(id);
    }

}
