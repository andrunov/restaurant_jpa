package ru.agorbunov.restaurant.util;

import ru.agorbunov.restaurant.model.Dish;
import ru.agorbunov.restaurant.model.Order;

import java.util.Comparator;

/**
 * Class for different comparing methods
 */
public class ComparatorUtil {
    /*comparator for Dish class*/
    public static Comparator<Dish> dishComparator = new Comparator<Dish>() {
        @Override
        public int compare(Dish o1, Dish o2) {
            return o1.getDescription().compareTo(o2.getDescription());
        }
    };

    /*comparator for Order class*/
    public static Comparator<Order> orderComparator = new Comparator<Order>() {
        @Override
        public int compare(Order o1, Order o2) {
            return o1.getId()-o2.getId();
        }
    };
}
