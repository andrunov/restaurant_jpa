package ru.agorbunov.restaurant;

import ru.agorbunov.restaurant.matcher.ModelMatcher;
import ru.agorbunov.restaurant.model.Dish;
import ru.agorbunov.restaurant.model.Order;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.agorbunov.restaurant.DishTestData.*;
import static ru.agorbunov.restaurant.RestaurantTestData.*;
import static ru.agorbunov.restaurant.UserTestData.*;

/**
 * Created by Admin on 21.01.2017.
 */
public class OrderTestData {

    public static final ModelMatcher<Order> MATCHER = new ModelMatcher<>();

    public static final Order ORDER_01 = new Order(USER_01,RESTAURANT_01, LocalDateTime.of(2017,1,15,15,47));
    public static final Order ORDER_02 = new Order(USER_02,RESTAURANT_01, LocalDateTime.of(2017,1,14,18,49));
    public static final Order ORDER_03 = new Order(USER_03,RESTAURANT_02, LocalDateTime.of(2017,1,15,12,7));
    public static final Order ORDER_04 = new Order(USER_04,RESTAURANT_02, LocalDateTime.of(2017,1,15,2,9));
    public static final Order ORDER_05 = new Order(USER_05,RESTAURANT_03, LocalDateTime.of(2017,1,15,14,17));
    public static final Order ORDER_06 = new Order(USER_06,RESTAURANT_04, LocalDateTime.of(2017,1,15,9,29));
    public static final Order ORDER_07 = new Order(USER_06,RESTAURANT_04, LocalDateTime.of(2017,2,15,16,32));
    public static final Order ORDER_08 = new Order(USER_06,RESTAURANT_04, LocalDateTime.of(2017,2,15,18,28));

    public static final Order ORDER_CREATED = new Order(USER_01,RESTAURANT_01, LocalDateTime.of(2017,1,16,9,30));

    public static final int ORDER_01_ID = 100010;
    public static final int ORDER_05_ID = 100014;
    public static final int ORDER_06_ID = 100015;
    public static final int ORDER_07_ID = 100040;
    public static final int ORDER_08_ID = 100041;
    public static final int USER_01_ID = 100000;
    public static final int USER_06_ID = 100005;
    public static final int RESTAURANT_01_ID = 100006;
    public static final int RESTAURANT_02_ID = 100007;
    public static final int RESTAURANT_04_ID = 100009;
    public static final int DISH_01_ID = 100020;
    public static final int DISH_02_ID = 100021;
    public static final int DISH_03_ID = 100022;
    public static final int DISH_04_ID = 100023;
    public static final int DISH_15_ID = 100034;
    public static final List<Integer> ORDER_06_DISH_QUANTITY = Arrays.asList(1, 2, 3, 1, 1, 1);
    public static final LocalDateTime DATE_2017_01_15 = LocalDateTime.of(2017,1,15,16,32);
    public static final LocalDateTime DATE_2017_01_14 = LocalDateTime.of(2017,1,14, 0,0);
    public static final LocalDateTime DATE_2017_02_15 = LocalDateTime.of(2017,2,15,18,28);


    static {

        Map<Dish,Integer> dishes01 = new HashMap<>();
        dishes01.put(DISH_01,1);
        dishes01.put(DISH_02,2);
        dishes01.put(DISH_04,1);
        ORDER_01.setDishes(dishes01);

        Map<Dish,Integer> dishes02 = new HashMap<>();
        dishes02.put(DISH_01,1);
        dishes02.put(DISH_02,1);
        dishes02.put(DISH_04,2);
        dishes02.put(DISH_05,1);
        ORDER_02.setDishes(dishes02);

        Map<Dish,Integer> dishes03 = new HashMap<>();
        dishes03.put(DISH_06,1);
        dishes03.put(DISH_07,2);
        dishes03.put(DISH_08,1);
        ORDER_03.setDishes(dishes03);

        Map<Dish,Integer> dishes04 = new HashMap<>();
        dishes04.put(DISH_08,1);
        dishes04.put(DISH_09,2);
        ORDER_04.setDishes(dishes04);

        Map<Dish,Integer> dishes05 = new HashMap<>();
        dishes05.put(DISH_10,1);
        dishes05.put(DISH_11,2);
        ORDER_05.setDishes(dishes05);

        Map<Dish,Integer> dishes06 = new HashMap<>();
        dishes06.put(DISH_15,1);
        dishes06.put(DISH_16,1);
        dishes06.put(DISH_17,1);
        dishes06.put(DISH_18,2);
        dishes06.put(DISH_19,3);
        dishes06.put(DISH_20,1);
        ORDER_06.setDishes(dishes06);

        Map<Dish,Integer> dishes07 = new HashMap<>();
        dishes07.put(DISH_15,1);
        dishes07.put(DISH_16,1);
        dishes07.put(DISH_17,1);
        dishes07.put(DISH_18,2);
        dishes07.put(DISH_19,3);
        dishes07.put(DISH_20,1);
        ORDER_07.setDishes(dishes07);

        Map<Dish,Integer> dishes08 = new HashMap<>();
        dishes08.put(DISH_15,1);
        dishes08.put(DISH_16,1);
        dishes08.put(DISH_17,1);
        dishes08.put(DISH_18,2);
        dishes08.put(DISH_19,3);
        dishes08.put(DISH_20,1);
        ORDER_08.setDishes(dishes08);

        Map<Dish,Integer> dishes09 = new HashMap<>();
        dishes09.put(DISH_01,1);
        dishes09.put(DISH_02,1);
        ORDER_CREATED.setDishes(dishes09);
        ORDER_CREATED.setTotalPrice(4.7);
    }
}
