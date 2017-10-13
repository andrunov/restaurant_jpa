package ru.agorbunov.restaurant.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.agorbunov.restaurant.MenuListTestData;
import ru.agorbunov.restaurant.OrderTestData;
import ru.agorbunov.restaurant.matcher.ModelMatcher;
import ru.agorbunov.restaurant.model.Dish;
import ru.agorbunov.restaurant.model.Order;
import ru.agorbunov.restaurant.util.exception.NotFoundException;

import java.util.Arrays;
import java.util.Collections;

import static ru.agorbunov.restaurant.DishTestData.*;

/**
 * Created by Admin on 27.01.2017.
 */
public class DishServiceImplTest extends AbstractServiceTest {

    @Autowired
    protected DishService service;

    @Test
    public void save() throws Exception {
        service.save(DISH_CREATED,MENU_LIST_01_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(DISH_01,DISH_02,DISH_03,DISH_04,DISH_05, DISH_06, DISH_07, DISH_08, DISH_09, DISH_10, DISH_11, DISH_12, DISH_13, DISH_14, DISH_15, DISH_16, DISH_17, DISH_18, DISH_19, DISH_20, DISH_CREATED),service.getAll());
    }

    // orders must not clear during this method
    @Test
    public void saveWithoutOrders() throws Exception {
        Dish dish = service.get(DISH_05_ID,MENU_LIST_01_ID);
        service.save(dish,MENU_LIST_01_ID);
        OrderTestData.MATCHER.assertCollectionEquals(
                Collections.singletonList(OrderTestData.ORDER_02),
                service.getWithOrders(DISH_05_ID, MENU_LIST_01_ID).getOrders().keySet());
    }

    @Test
    public void saveNull() throws Exception{
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("dish must not be null");
        service.save(null,MENU_LIST_01_ID);
    }

    @Test
    public void delete() throws Exception {
        service.delete(DISH_01_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(DISH_02,DISH_03,DISH_04,DISH_05, DISH_06, DISH_07, DISH_08, DISH_09, DISH_10, DISH_11, DISH_12, DISH_13, DISH_14, DISH_15, DISH_16, DISH_17, DISH_18, DISH_19, DISH_20),service.getAll());

    }

    @Test
    public void deleteNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format("Not found entity with id=%d", 10));
        service.delete(10);
    }

    @Test
    public void getAll() throws Exception {
        MATCHER.assertCollectionEquals(Arrays.asList(DISH_01,DISH_02,DISH_03,DISH_04,DISH_05, DISH_06, DISH_07, DISH_08, DISH_09, DISH_10, DISH_11, DISH_12, DISH_13, DISH_14, DISH_15, DISH_16, DISH_17, DISH_18, DISH_19, DISH_20),service.getAll());
    }

    @Test
    public void get() throws Exception {
        Dish dish = service.get(DISH_01_ID,MENU_LIST_01_ID);
        MATCHER.assertEquals(DISH_01, dish);
    }

    @Test
    public void getNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format("Not found entity with id=%d", 10));
        service.get(DISH_01_ID,10);
    }

    @Test
    public  void update() throws Exception{
        Dish dish = service.get(DISH_01_ID,MENU_LIST_01_ID);
        dish.setDescription("обновленное блюдо");
        dish.setPrice(1.02);
        service.save(dish, MENU_LIST_01_ID);
        MATCHER.assertEquals(dish, service.get(DISH_01_ID,MENU_LIST_01_ID));
    }

    @Test
    public void updateNotFound() throws Exception{
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format("Not found entity with id=%d", DISH_01_ID));
        Dish dish = service.get(DISH_01_ID,MENU_LIST_01_ID);
        service.save(dish, MENU_LIST_02_ID);
    }

    @Test
    public void updateNull() throws Exception{
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("dish must not be null");
        service.save(null,MENU_LIST_01_ID);
    }

    @Test
    public void getWith() throws Exception {
        ModelMatcher<Order> OrderMatcher = new ModelMatcher<>();
        ModelMatcher<Integer> IntegerMatcher = new ModelMatcher<>();
        Dish dish = service.getWithOrders(DISH_01_ID,MENU_LIST_01_ID);
        MATCHER.assertEquals(DISH_01, dish);
        OrderMatcher.assertCollectionEquals(Arrays.asList(OrderTestData.ORDER_01,OrderTestData.ORDER_02),dish.getOrders().keySet());
        IntegerMatcher.assertCollectionEquals(DISH_01_IN_ORDERS_QUANTITY,dish.getOrders().values());
    }

    @Test
    public void getWithNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format("Not found entity with id=%d", 10));
        service.getWithOrders(DISH_01_ID,10);
    }

    @Test
    public void getByMenuList() throws Exception{
        MATCHER.assertCollectionEquals(Arrays.asList(DISH_01, DISH_02, DISH_03, DISH_04, DISH_05),service.getByMenuList(MenuListTestData.MENU_LIST_01_ID));
    }

    @Test
    public void getByOrder() throws Exception{
        MATCHER.assertCollectionEquals(Arrays.asList(DISH_16, DISH_18, DISH_19, DISH_20, DISH_17, DISH_15),service.getByOrder(OrderTestData.ORDER_06_ID).keySet());
        ModelMatcher<Integer> integerMatcher = new ModelMatcher<>();
        integerMatcher.assertCollectionEquals(OrderTestData.ORDER_06_DISH_QUANTITY,service.getByOrder(OrderTestData.ORDER_06_ID).values());
    }

    @Test
    public void deleteFromOrder() throws Exception{
        service.deleteFromOrder(DISH_01_ID,ORDER_02_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(DISH_05,DISH_04,DISH_02),service.getByOrder(ORDER_02_ID).keySet());
        ModelMatcher<Integer> integerMatcher = new ModelMatcher<>();
        integerMatcher.assertCollectionEquals(Arrays.asList(1,2,1),service.getByOrder(ORDER_02_ID).values());

    }


}