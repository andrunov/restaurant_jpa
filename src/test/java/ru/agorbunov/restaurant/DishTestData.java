package ru.agorbunov.restaurant;

import ru.agorbunov.restaurant.matcher.ModelMatcher;
import ru.agorbunov.restaurant.model.Dish;

import java.util.Arrays;
import java.util.List;

import static ru.agorbunov.restaurant.MenuListTestData.*;

/**
 * Created by Admin on 20.01.2017.
 */
public class DishTestData {

    public static final ModelMatcher<Dish> MATCHER = new ModelMatcher<>();

    public static final Dish DISH_01 = new Dish("Каша овсяная",1.25);
    public static final Dish DISH_02 = new Dish("Сырники",3.45);
    public static final Dish DISH_03 = new Dish("Блины",2.48);
    public static final Dish DISH_04 = new Dish("Суп гороховый",5.57);
    public static final Dish DISH_05 = new Dish("Рассольник",6.87);
    public static final Dish DISH_06 = new Dish("Жульен с грибами",12.47);
    public static final Dish DISH_07 = new Dish("Пельмени",7.96);
    public static final Dish DISH_08 = new Dish("Котлета по киевски",14.58);
    public static final Dish DISH_09 = new Dish("Чай черный",0.55);
    public static final Dish DISH_10 = new Dish("Чай зеленый",0.55);
    public static final Dish DISH_11 = new Dish("Кофе черный",0.75);
    public static final Dish DISH_12 = new Dish("Кофе белый",0.95);
    public static final Dish DISH_13 = new Dish("Котлета по питерски",12.54);
    public static final Dish DISH_14 = new Dish("Поросенок под хреном",24.58);
    public static final Dish DISH_15 = new Dish("Чебурек",4.62);
    public static final Dish DISH_16 = new Dish("Беляш",4.12);
    public static final Dish DISH_17 = new Dish("Чай черный с лимоном",1.95);
    public static final Dish DISH_18 = new Dish("Борщ",17.58);
    public static final Dish DISH_19 = new Dish("Плов узбекский",12.75);
    public static final Dish DISH_20 = new Dish("Салат оливье",8.12);

    public static final Dish DISH_CREATED = new Dish("Созданная еда",3.12);

    public static final int DISH_01_ID = 100020;
    public static final int DISH_02_ID = 100021;
    public static final int DISH_05_ID = 100024;
    public static final int DISH_10_ID = 100029;
    public static final int MENU_LIST_01_ID = 100016;
    public static final int MENU_LIST_02_ID = 100017;
    public static final int ORDER_01_ID = 100010;
    public static final int ORDER_02_ID = 100011;
    public static final List<Integer> DISH_01_IN_ORDERS_QUANTITY = Arrays.asList(1, 1);


    static {
        DISH_01.setMenuList(MENU_LIST_01); //  ORDER_01, ORDER_02
        DISH_02.setMenuList(MENU_LIST_01); //  ORDER_01, ORDER_02
        DISH_03.setMenuList(MENU_LIST_01); //  no orders
        DISH_04.setMenuList(MENU_LIST_01); //  ORDER_01, ORDER_02
        DISH_05.setMenuList(MENU_LIST_01); //  ORDER_02
        DISH_06.setMenuList(MENU_LIST_02); //  ORDER_03
        DISH_07.setMenuList(MENU_LIST_02); //  ORDER_03
        DISH_08.setMenuList(MENU_LIST_02); //  ORDER_03, ORDER_04
        DISH_09.setMenuList(MENU_LIST_02); //  ORDER_04
        DISH_10.setMenuList(MENU_LIST_03); //  ORDER_05
        DISH_11.setMenuList(MENU_LIST_03); //  ORDER_05
        DISH_12.setMenuList(MENU_LIST_03); //  no orders
        DISH_13.setMenuList(MENU_LIST_03); //  no orders
        DISH_14.setMenuList(MENU_LIST_03); //  no orders
        DISH_15.setMenuList(MENU_LIST_04); //  ORDER_06
        DISH_16.setMenuList(MENU_LIST_04); //  ORDER_06
        DISH_17.setMenuList(MENU_LIST_04); //  ORDER_06
        DISH_18.setMenuList(MENU_LIST_04); //  ORDER_06
        DISH_19.setMenuList(MENU_LIST_04); //  ORDER_06
        DISH_20.setMenuList(MENU_LIST_04); //  ORDER_06
        DISH_CREATED.setMenuList(MENU_LIST_01); //  ORDER_01

    }

}
