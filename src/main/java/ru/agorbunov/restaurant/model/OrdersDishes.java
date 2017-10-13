package ru.agorbunov.restaurant.model;

import ru.agorbunov.restaurant.model.jpa.OrdersDishesId;

import javax.persistence.*;

/**
 * Entity represents many-to0many relationship between Order and Dish
 * with one additional field - quantity of dishes
 */
@Entity
@Table(name="orders_dishes")
@IdClass(OrdersDishesId.class)
public class OrdersDishes {

    /*represents Order*/
    @Id
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    /*represents Dish*/
    @Id
    @ManyToOne
    @JoinColumn(name = "dish_id", referencedColumnName = "id")
    private Dish dish;

    /*represents quantity of dishes*/
    @Column(name = "dish_quantity", nullable = false)
    private int dishQuantity;

    public int getDishQuantity() {
        return dishQuantity;
    }

    public void setDishQuantity(int dishQuantity) {
        this.dishQuantity = dishQuantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }
}
