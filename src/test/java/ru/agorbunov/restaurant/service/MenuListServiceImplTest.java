package ru.agorbunov.restaurant.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.agorbunov.restaurant.DishTestData;
import ru.agorbunov.restaurant.matcher.ModelMatcher;
import ru.agorbunov.restaurant.model.Dish;
import ru.agorbunov.restaurant.model.MenuList;
import ru.agorbunov.restaurant.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ru.agorbunov.restaurant.MenuListTestData.*;

/**
 * Created by Admin on 30.01.2017.
 */
public class MenuListServiceImplTest extends AbstractServiceTest {

    @Autowired
    private MenuListService service;

    @Test
    public void save() throws Exception {
        service.save(MENU_LIST_CREATED,RESTAURANT_01_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(MENU_LIST_CREATED,MENU_LIST_01,MENU_LIST_02,MENU_LIST_03,MENU_LIST_04),service.getAll());
    }

    @Test
    public void saveNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("menu list must not be null");
        service.save(null,RESTAURANT_01_ID);
    }

    @Test
    public void delete() throws Exception {
        service.delete(MENU_LIST_01_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(MENU_LIST_02,MENU_LIST_03,MENU_LIST_04),service.getAll());
    }

    @Test
    public void deleteNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format("Not found entity with id=%d", 10));
        service.delete(10);
    }

    @Test
    public void getAll() throws Exception {
        MATCHER.assertCollectionEquals(Arrays.asList(MENU_LIST_01,MENU_LIST_02,MENU_LIST_03,MENU_LIST_04),service.getAll());
    }

    @Test
    public void get() throws Exception {
        MATCHER.assertEquals(MENU_LIST_01,service.get(MENU_LIST_01_ID, RESTAURANT_01_ID));
    }

    @Test
    public void getNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format("Not found entity with id=%d", MENU_LIST_01_ID));
        service.get(MENU_LIST_01_ID, RESTAURANT_02_ID);
    }

    @Test
    public void update() throws Exception{
        MenuList menuList = service.get(MENU_LIST_01_ID, RESTAURANT_01_ID);
        menuList.setDateTime(LocalDateTime.of(2017,2,15,17,31));
        menuList.setEnabled(false);
        service.save(menuList,RESTAURANT_01_ID);
        MATCHER.assertEquals(menuList,service.get(MENU_LIST_01_ID, RESTAURANT_01_ID));
    }

    @Test
    public void updateNotFound() throws Exception{
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format("Not found entity with id=%d", MENU_LIST_01_ID));
        MenuList menuList = service.get(MENU_LIST_01_ID, RESTAURANT_01_ID);
        service.save(menuList, RESTAURANT_02_ID);
    }

    @Test
    public void updateNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("menu list must not be null");
        service.save(null,RESTAURANT_01_ID);
    }

    @Test
    public void getWith() throws Exception {
        ModelMatcher<Dish> MatcherDish = new ModelMatcher<>();
        MenuList menuList = service.getWithDishes(MENU_LIST_01_ID, RESTAURANT_01_ID);
        MATCHER.assertEquals(MENU_LIST_01,menuList);
        MatcherDish.assertCollectionEquals(MENU_LIST_01.getDishList(),menuList.getDishList());
    }

    @Test
    public void getWithNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format("Not found entity with id=%d", MENU_LIST_01_ID));
        service.getWithDishes(MENU_LIST_01_ID, RESTAURANT_02_ID);
    }

    @Test
    public void getByRestaurant() throws Exception {
        MATCHER.assertCollectionEquals(Collections.singletonList(MENU_LIST_01),service.getByRestaurant(RESTAURANT_01_ID));
    }

    @Test
    public void getByRestaurantAndEnabled() throws Exception {
        MATCHER.assertCollectionEquals(Collections.singletonList(MENU_LIST_01),service.getByRestaurantAndEnabled(RESTAURANT_01_ID, true));
        MATCHER.assertCollectionEquals(Collections.emptyList(),service.getByRestaurantAndEnabled(RESTAURANT_01_ID, false));
    }

    @Test
    public void getByDish() throws Exception {
        MATCHER.assertEquals(MENU_LIST_01,service.getByDish(DishTestData.DISH_01_ID));
        MATCHER.assertEquals(MENU_LIST_03,service.getByDish(DishTestData.DISH_10_ID));
    }

    @Test
    public void getByDishWith() throws Exception {
        ModelMatcher<Dish> MatcherDish = new ModelMatcher<>();
        List<Dish> dishes = service.getByDish(DishTestData.DISH_01_ID).getDishList();
        MatcherDish.assertCollectionEquals(MENU_LIST_01.getDishList(),dishes);
    }
}