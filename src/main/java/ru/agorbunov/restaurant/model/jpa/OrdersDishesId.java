package ru.agorbunov.restaurant.model.jpa;

import java.io.Serializable;

/**
 * Class use as Id in many-to-mani relationship between Order and Dish
 */
public class OrdersDishesId implements Serializable {

    /*order id*/
    private int order;

    /*dish id*/
    private int dish;

    public OrdersDishesId() {
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getDish() {
        return dish;
    }

    public void setDish(int dish) {
        this.dish = dish;
    }
}
