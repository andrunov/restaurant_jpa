package ru.agorbunov.restaurant.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.agorbunov.restaurant.model.MenuList;
import ru.agorbunov.restaurant.model.Restaurant;
import ru.agorbunov.restaurant.service.MenuListService;
import ru.agorbunov.restaurant.service.RestaurantService;
import ru.agorbunov.restaurant.util.DateTimeUtil;
import ru.agorbunov.restaurant.util.ValidationUtil;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Rest controller for menuLists.jsp and other .jsp
 * to exchange menuList data with menuListService-layer
 */
@RestController
@RequestMapping(value =  "/ajax/menuLists")
public class MenuListAjaxController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MenuListService menuListService;

    @Autowired
    private RestaurantService restaurantService;

    /*get all menu lists by current restaurant*/
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MenuList> getByRestaurant() {
        log.info("getByRestaurant");
        Restaurant currentRestaurant = CurrentEntities.getCurrentRestaurant();
        return menuListService.getByRestaurant(currentRestaurant.getId());
    }

    /*get all menu lists by current restaurant and enabled status key*/
    @GetMapping(value = "/filterByEnabled/{enabled}",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MenuList> getByRestaurantAndEnabled(@PathVariable("enabled") boolean enabled) {
        log.info("getByRestaurantAndEnabled");
        Restaurant currentRestaurant = CurrentEntities.getCurrentRestaurant();
        return menuListService.getByRestaurantAndEnabled(currentRestaurant.getId(),enabled);
    }

    /*get all menu lists by current restaurant Id pass as 1st parameter
     and enabled pass as 2nd parameter*/
    @GetMapping(value = "byRestaurant/{restaurantId}&{enabled}",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MenuList> getByRestaurantIdAndEnabled(@PathVariable("restaurantId") int restaurantId,
                                                      @PathVariable("enabled") String enabled) {
        log.info("byRestaurantIdAndEnabled/{restaurantId}&{enabledKey}");
        Restaurant currentRestaurant = restaurantService.get(restaurantId);
        CurrentEntities.setCurrentRestaurant(currentRestaurant);
        List<MenuList> result = null;
        if (enabled.equals("ALL")){
            result = menuListService.getByRestaurant(restaurantId);
        }else {
            result = menuListService.getByRestaurantAndEnabled(restaurantId,Boolean.parseBoolean(enabled));
        }
        return result;
    }

    /*get menuList by Id and current restaurant*/
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public MenuList getMenuList(@PathVariable("id") int id) {
        log.info("get " + id);
        Restaurant currentRestaurant = CurrentEntities.getCurrentRestaurant();
        return menuListService.get(id,currentRestaurant.getId());
    }

    /*delete menuList by Id*/
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") int id) {
        log.info("delete " + id);
        menuListService.delete(id);
    }

    /*create new menuList or update if exist*/
    @PostMapping
    public void createOrUpdate(@RequestParam("id") Integer id,
                               @RequestParam("description")String description,
                               @RequestParam("dateTime")@DateTimeFormat(pattern = DateTimeUtil.DATE_TIME_PATTERN) LocalDateTime dateTime,
                               @RequestParam(value = "enabled",required = false) boolean enabled){
        Restaurant currentRestaurant = CurrentEntities.getCurrentRestaurant();
        MenuList menuList = new MenuList(currentRestaurant, description, dateTime);
        menuList.setId(id);
        menuList.setEnabled(enabled);
        checkEmpty(menuList);
        if (menuList.isNew()) {
            ValidationUtil.checkNew(menuList);
            log.info("create " + menuList);
            menuListService.save(menuList,currentRestaurant.getId());
        } else {
            log.info("update " + menuList);
            menuListService.save(menuList,currentRestaurant.getId());
        }
    }

    /*check menuList for empty fields*/
    private void checkEmpty(MenuList menuList){
        ValidationUtil.checkEmpty(menuList.getDateTime(),"dateTime");
    }


}
