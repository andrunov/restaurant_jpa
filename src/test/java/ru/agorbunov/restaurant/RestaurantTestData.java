package ru.agorbunov.restaurant;

import ru.agorbunov.restaurant.matcher.ModelMatcher;
import ru.agorbunov.restaurant.model.Restaurant;

import java.util.Arrays;
import java.util.Collections;

import static ru.agorbunov.restaurant.MenuListTestData.*;
import static ru.agorbunov.restaurant.OrderTestData.*;

/**
 * Created by Admin on 21.01.2017.
 */
public class RestaurantTestData {

    public static final ModelMatcher<Restaurant> MATCHER = new ModelMatcher<>();

    public static final Restaurant RESTAURANT_01 = new Restaurant("Ёлки-палки","ул. Некрасова, 14");
    public static final Restaurant RESTAURANT_02 = new Restaurant("Пиццерия","пл. Пушкина, 6");
    public static final Restaurant RESTAURANT_03 = new Restaurant("Закусочная","Привокзальная пл, 3");
    public static final Restaurant RESTAURANT_04 = new Restaurant("Прага","ул. Арбат, 1");
    public static final Restaurant RESTAURANT_CREATED = new Restaurant("Созданный ресторант","ул. Новая, 1");

    public static int RESTAURANT_01_ID = 100006;
    public static int RESTAURANT_02_ID = 100007;
    public static int RESTAURANT_03_ID = 100008;

    static {
        RESTAURANT_01.setMenuLists(Collections.singletonList(MENU_LIST_01));
        RESTAURANT_01.setOrders(Arrays.asList(ORDER_01,ORDER_02));
        RESTAURANT_02.setMenuLists(Collections.singletonList(MENU_LIST_02));
        RESTAURANT_02.setOrders(Arrays.asList(ORDER_03,ORDER_04));
        RESTAURANT_03.setMenuLists(Collections.singletonList(MENU_LIST_03));
        RESTAURANT_03.setOrders(Collections.singletonList(ORDER_05));
        RESTAURANT_04.setMenuLists(Collections.singletonList(MENU_LIST_04));
        RESTAURANT_04.setOrders(Collections.singletonList(ORDER_06));
        RESTAURANT_CREATED.setMenuLists(Collections.singletonList(MENU_LIST_01));
        RESTAURANT_CREATED.setOrders(Collections.singletonList(ORDER_01));
    }


}
