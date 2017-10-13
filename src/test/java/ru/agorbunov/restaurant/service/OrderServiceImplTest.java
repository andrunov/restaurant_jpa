package ru.agorbunov.restaurant.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.agorbunov.restaurant.DishTestData;
import ru.agorbunov.restaurant.RestaurantTestData;
import ru.agorbunov.restaurant.UserTestData;
import ru.agorbunov.restaurant.matcher.ModelMatcher;
import ru.agorbunov.restaurant.model.Dish;
import ru.agorbunov.restaurant.model.Order;
import ru.agorbunov.restaurant.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static ru.agorbunov.restaurant.OrderTestData.*;

/**
 * Created by Admin on 30.01.2017.
 */
public class OrderServiceImplTest extends AbstractServiceTest {

    @Autowired
    protected OrderService service;

    @Test
    public void save() throws Exception {
        int dishIds[] = {DISH_01_ID,DISH_02_ID};
        int dishQuantityValues[] = {1,1};
        Order orderSaved = service.save(ORDER_CREATED,USER_01_ID,RESTAURANT_01_ID, dishIds,dishQuantityValues);
        int orderSavedId = orderSaved.getId();
        orderSaved = service.getWithDishes(orderSavedId,USER_01_ID,RESTAURANT_01_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(ORDER_08,ORDER_07,ORDER_CREATED,ORDER_01,ORDER_05,ORDER_03,ORDER_06,ORDER_04,ORDER_02),service.getAll());
        ModelMatcher<Dish> DishMatcher = new ModelMatcher<>();
        DishMatcher.assertCollectionEquals(ORDER_CREATED.getDishes().keySet(), orderSaved.getDishes().keySet());
        ModelMatcher<Integer> IntegerMatcher = new ModelMatcher<>();
        IntegerMatcher.assertCollectionEquals(ORDER_CREATED.getDishes().values(), orderSaved.getDishes().values());
    }

    @Test
    public void updateWithoutDishes() throws Exception {
        Order order = service.get(ORDER_05_ID, UserTestData.USER_05_ID, RestaurantTestData.RESTAURANT_03_ID);
        order.setDateTime( LocalDateTime.of(2017,3,16,19,56));
        service.save(order, UserTestData.USER_05_ID, RestaurantTestData.RESTAURANT_03_ID);
        MATCHER.assertEquals(order,service.get(ORDER_05_ID, UserTestData.USER_05_ID, RestaurantTestData.RESTAURANT_03_ID));
        DishTestData.MATCHER.assertCollectionEquals(Arrays.asList(DishTestData.DISH_11, DishTestData.DISH_10),
                service.getWithDishes(ORDER_05_ID, UserTestData.USER_05_ID, RestaurantTestData.RESTAURANT_03_ID).getDishes().keySet());
    }

    @Test
    public void saveNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("order must not be null");
        int dishIds[] = {DISH_01_ID,DISH_02_ID};
        int dishQuantityValues[] = {1,1};
        service.save(null,USER_01_ID,RESTAURANT_01_ID, dishIds,dishQuantityValues);
    }

    @Test
    public void delete() throws Exception {
        service.delete(ORDER_01_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(ORDER_08,ORDER_07,ORDER_05,ORDER_03,ORDER_06,ORDER_04,ORDER_02),service.getAll());

    }

    @Test
    public void deleteNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.delete(1);
    }

    @Test
    public void getAll() throws Exception {
        MATCHER.assertCollectionEquals(Arrays.asList(ORDER_08,ORDER_07,ORDER_01,ORDER_05,ORDER_03,ORDER_06,ORDER_04,ORDER_02),service.getAll());
    }

    @Test
    public void get() throws Exception {
        MATCHER.assertEquals(ORDER_01,service.get(ORDER_01_ID,USER_01_ID,RESTAURANT_01_ID));
    }

    @Test
    public void getNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format("Not found entity with id=%d", ORDER_01_ID));
        service.get(ORDER_01_ID,USER_01_ID,RESTAURANT_02_ID);
    }

    @Test
    public void update() throws Exception{
        Order order = service.get(ORDER_01_ID,USER_01_ID,RESTAURANT_01_ID);
        order.setDateTime( LocalDateTime.of(2017,2,16,17,46));
        int dishIds[] = {DISH_01_ID,DISH_02_ID,DISH_03_ID,DISH_04_ID};
        int dishQuantityValues[] = {1,2,3,4};
        service.save(order,USER_01_ID,RESTAURANT_01_ID,dishIds,dishQuantityValues);
        Order orderSaved = service.getWithDishes(ORDER_01_ID,USER_01_ID,RESTAURANT_01_ID);
        MATCHER.assertEquals(order, orderSaved);
        ModelMatcher<Dish> DishMatcher = new ModelMatcher<>();
        DishMatcher.assertCollectionEquals(Arrays.asList(DishTestData.DISH_03,
                                                        DishTestData.DISH_01,
                                                        DishTestData.DISH_04,
                                                        DishTestData.DISH_02),
                                                        orderSaved.getDishes().keySet());
        ModelMatcher<Integer> IntegerMatcher = new ModelMatcher<>();
        IntegerMatcher.assertCollectionEquals(Arrays.asList(3,1,4,2),orderSaved.getDishes().values());
    }

    @Test
    public void updateNotFound() throws Exception{
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format("Not found entity with id=%d", ORDER_01_ID));
        Order order = service.get(ORDER_01_ID,USER_01_ID,RESTAURANT_01_ID);
        service.save(order,USER_01_ID,RESTAURANT_02_ID);
    }

    @Test
    public void updateNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("order must not be null");
        int dishIds[] = {DISH_01_ID,DISH_02_ID};
        int dishQuantityValues[] = {1,1};
        service.save(null,USER_01_ID,RESTAURANT_01_ID, dishIds,dishQuantityValues);
    }

    @Test
    public void getWith() throws Exception {
        ModelMatcher<Dish> DishMatcher = new ModelMatcher<>();
        ModelMatcher<Integer> IntegerMatcher = new ModelMatcher<>();
        Order order = service.getWithDishes(ORDER_06_ID,USER_06_ID,RESTAURANT_04_ID);
        MATCHER.assertEquals(ORDER_06,order);
        DishMatcher.assertCollectionEquals(ORDER_06.getDishes().keySet(),order.getDishes().keySet());
        IntegerMatcher.assertCollectionEquals(ORDER_06.getDishes().values(),order.getDishes().values());
    }

    @Test
    public void getWithNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format("Not found entity with id=%d", ORDER_01_ID));
        service.getWithDishes(ORDER_01_ID,USER_01_ID,RESTAURANT_02_ID);
    }

    @Test
    public void getByUser() throws Exception{
        MATCHER.assertCollectionEquals(Collections.singletonList(ORDER_01),service.getByUser(USER_01_ID));
    }

    @Test
    public void getByUserAndStatus() throws Exception{
        MATCHER.assertCollectionEquals(Collections.singletonList(ORDER_01),service.getByUserAndStatus(USER_01_ID, "ACCEPTED"));
        MATCHER.assertCollectionEquals(Collections.emptyList(),service.getByUserAndStatus(USER_01_ID, "FINISHED"));
    }

    @Test
    public void getByDish() throws Exception{
        MATCHER.assertCollectionEquals(Arrays.asList(ORDER_01,ORDER_02),service.getByDish(DISH_02_ID));
    }

    @Test
    public void getByDishAndStatus() throws Exception{
        MATCHER.assertCollectionEquals(Arrays.asList(ORDER_01,ORDER_02),service.getByDishAndStatus(DISH_02_ID, "ACCEPTED"));
        MATCHER.assertCollectionEquals(Collections.emptyList(),service.getByDishAndStatus(DISH_02_ID, "FINISHED"));
    }

    @Test
    public void getByUserAndDate() throws Exception{
        MATCHER.assertCollectionEquals(Collections.singletonList(ORDER_06),service.getByUserAndDate(USER_06_ID, DATE_2017_01_15));
        MATCHER.assertCollectionEquals(Arrays.asList(ORDER_08,ORDER_07),service.getByUserAndDate(USER_06_ID, DATE_2017_02_15));
    }

    @Test
    public void getByUserAndStatusAndDate() throws Exception{
        MATCHER.assertCollectionEquals(Collections.singletonList(ORDER_06),service.getByUserAndStatusAndDate(USER_06_ID, "ACCEPTED",DATE_2017_01_15));
        MATCHER.assertCollectionEquals(Collections.singletonList(ORDER_08),service.getByUserAndStatusAndDate(USER_06_ID, "FINISHED",DATE_2017_02_15));
    }

    @Test
    public void getByDishAndDate() throws Exception{
        MATCHER.assertCollectionEquals(Collections.singletonList(ORDER_01),service.getByDishAndDate(DISH_01_ID, DATE_2017_01_15));
        MATCHER.assertCollectionEquals(Collections.singletonList(ORDER_02),service.getByDishAndDate(DISH_04_ID, DATE_2017_01_14));
        MATCHER.assertCollectionEquals(Arrays.asList(ORDER_08,ORDER_07),service.getByDishAndDate(DISH_15_ID, DATE_2017_02_15));
    }

    @Test
    public void getByDishAndStatusAndDate() throws Exception{
        MATCHER.assertCollectionEquals(Collections.singletonList(ORDER_01),service.getByDishAndStatusAndDate(DISH_01_ID, "ACCEPTED",DATE_2017_01_15));
        MATCHER.assertCollectionEquals(Collections.singletonList(ORDER_07),service.getByDishAndStatusAndDate(DISH_15_ID, "READY",DATE_2017_02_15));
        MATCHER.assertCollectionEquals(Collections.singletonList(ORDER_08),service.getByDishAndStatusAndDate(DISH_15_ID, "FINISHED",DATE_2017_02_15));
    }

}