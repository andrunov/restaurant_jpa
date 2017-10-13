package ru.agorbunov.restaurant.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.agorbunov.restaurant.DishTestData;
import ru.agorbunov.restaurant.OrderTestData;
import ru.agorbunov.restaurant.matcher.ModelMatcher;
import ru.agorbunov.restaurant.model.Order;
import ru.agorbunov.restaurant.model.User;
import ru.agorbunov.restaurant.util.exception.NotFoundException;

import java.util.Arrays;
import java.util.Collections;

import static ru.agorbunov.restaurant.UserTestData.*;


/**
 * Created by Admin on 28.01.2017.
 */
public class UserServiceImplTest extends AbstractServiceTest {


    @Autowired
    private UserService service;

    @Autowired
    private OrderService orderService;

    @Before
    public void setUp() throws Exception {
        service.evictCache();
    }

    @Test
    public void save() throws Exception {
        service.save(USER_CREATED);
        MATCHER.assertCollectionEquals(Arrays.asList(USER_01, USER_02, USER_03, USER_04, USER_05, USER_06, USER_CREATED), service.getAll());
    }

    @Test
    public void saveNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("user must not be null");
        service.save(null);
    }

    @Test
    public void delete() throws Exception {
        service.delete(USER_01_ID);
        MATCHER.assertCollectionEquals(Arrays.asList( USER_02, USER_03, USER_04, USER_05, USER_06), service.getAll());
    }

    @Test
    public void deleteNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format("Not found entity with id=%d", 10));
        service.delete(10);
    }

    @Test
    public void getAll() throws Exception {
        MATCHER.assertCollectionEquals(Arrays.asList(USER_01, USER_02, USER_03, USER_04, USER_05, USER_06), service.getAll());

    }

    @Test
    public void get() throws Exception {
        User user = service.get(USER_01_ID);
        MATCHER.assertEquals(USER_01, user);
        Assert.assertEquals(Collections.singletonList(USER_01.getRoles()),
                            Collections.singletonList(user.getRoles()));
    }

    @Test
    public void getByEmail() throws Exception {
        User user = service.getByEmail("jbj@gmail.com");
        MATCHER.assertEquals(USER_05, user);
    }

    @Test
    public void getNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(String.format("Not found entity with id=%d", 10));
        service.get(10);
    }

    @Test
    public void update() throws Exception{
        User user = service.get(USER_01_ID);
        user.setEmail("newmail@mail.ru");
        user.setName("обновленное имя");
        user.setEnabled(false);
        service.save(user);
        MATCHER.assertEquals(user,service.get(USER_01_ID));
    }


    @Test
    public void updateNull() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("user must not be null");
        service.save(null);
    }

    @Test
    public void getWith() throws Exception{
        ModelMatcher<Order> OrderMatcher = new ModelMatcher<>();
        User user = service.getWithOrders(USER_02_ID);
        OrderMatcher.assertCollectionEquals(USER_02.getOrders(),user.getOrders());
    }

    @Test
    public void accountAndSaveTotalOrdersAmount() throws Exception{
        orderService.save(OrderTestData.ORDER_CREATED,USER_01_ID,OrderTestData.RESTAURANT_01_ID,new int[] {DishTestData.DISH_01_ID, DishTestData.DISH_02_ID},new int[] {1,2});
        User user = service.get(USER_01_ID);
        Assert.assertEquals(18.42, user.getTotalOrdersAmount(),0.001);
    }

}