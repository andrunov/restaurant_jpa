package ru.agorbunov.restaurant.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;
import ru.agorbunov.restaurant.util.ComparatorUtil;
import ru.agorbunov.restaurant.util.DateTimeUtil;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Class represents order
 */
@SuppressWarnings("JpaQlInspection")
@NamedNativeQueries({
        @NamedNativeQuery(name = Order.GET_ALL_BY_DISH, query = "SELECT o.* FROM orders AS o JOIN orders_dishes AS od ON o.id = od.order_id WHERE od.dish_id=:dishId ORDER BY date_time DESC ",resultClass = Order.class),
        @NamedNativeQuery(name = Order.GET_ALL_BY_DISH_AND_STATUS, query = "SELECT o.* FROM orders AS o JOIN orders_dishes AS od ON o.id = od.order_id WHERE od.dish_id=:dishId and o.status=:status ORDER BY date_time DESC ",resultClass = Order.class),
        @NamedNativeQuery(name = Order.GET_ALL_BY_DISH_AND_DATE, query = "SELECT o.* FROM orders AS o JOIN orders_dishes AS od ON o.id = od.order_id WHERE od.dish_id=:dishId AND date_time>=:beginDate AND date_time<=:endDate ORDER BY date_time DESC ",resultClass = Order.class),
        @NamedNativeQuery(name = Order.GET_ALL_BY_DISH_AND_STATUS_AND_DATE, query = "SELECT o.* FROM orders AS o JOIN orders_dishes AS od ON o.id = od.order_id WHERE od.dish_id=:dishId AND o.STATUS=:status AND o.date_time>=:beginDate AND o.date_time<=:endDate ORDER BY o.date_time DESC",resultClass = Order.class)
})
@NamedQueries({
        @NamedQuery(name = Order.GET_ALL, query = "SELECT o from Order o order by o.dateTime desc "),
        @NamedQuery(name = Order.GET_ALL_BY_USER, query = "SELECT o from Order o join fetch o.restaurant where o.user.id=:userId order by o.dateTime desc "),
        @NamedQuery(name = Order.GET_ALL_BY_USER_AND_DATE, query = "SELECT o from Order o join fetch o.restaurant where o.user.id=:userId and o.dateTime>=:beginDate and o.dateTime<=:endDate order by o.dateTime desc "),
        @NamedQuery(name = Order.GET_ALL_BY_USER_AND_STATUS, query = "SELECT o from Order o join fetch o.restaurant where o.user.id=:userId and o.status=:status order by o.dateTime desc "),
        @NamedQuery(name = Order.GET_ALL_BY_USER_AND_STATUS_AND_DATE, query = "SELECT o from Order o join fetch o.restaurant where o.user.id=:userId and o.status=:status and o.dateTime>=:beginDate and o.dateTime<=:endDate order by o.dateTime desc "),
        @NamedQuery(name = Order.GET_WITH_USER, query = "SELECT o from Order o join fetch o.user where o.id=:id order by o.dateTime desc "),
        @NamedQuery(name = Order.GET_WITH_DISHES, query = "SELECT o from Order o join fetch o.dishes where o.id=:id"),
        @NamedQuery(name = Order.DELETE, query = "DELETE FROM Order o WHERE o.id=:id"),
        @NamedQuery(name = Order.DELETE_ORDERS_DISHES, query = "DELETE FROM OrdersDishes od WHERE od.order.id=:id")
})
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    public static final String GET_ALL = "Order.getAll";
    public static final String GET_ALL_BY_USER = "Order.getAllbyUser";
    public static final String GET_ALL_BY_USER_AND_STATUS = "Order.getAllbyUserAndStatus";
    public static final String GET_ALL_BY_USER_AND_DATE = "Order.getAllbyUserAndDate";
    public static final String GET_ALL_BY_USER_AND_STATUS_AND_DATE = "Order.getAllbyUserAndStatusAndDate";
    public static final String GET_WITH_USER = "Order.getWithUser";
    public static final String GET_ALL_BY_DISH = "Order.getAllbyDish";
    public static final String GET_ALL_BY_DISH_AND_STATUS = "Order.getAllbyDishAndStatus";
    public static final String GET_ALL_BY_DISH_AND_DATE = "Order.getAllbyDishAndDate";
    public static final String GET_ALL_BY_DISH_AND_STATUS_AND_DATE = "Order.getAllbyDishAndStatusAndDate";
    public static final String DELETE = "Order.delete";
    static final String DELETE_ORDERS_DISHES = "Order.deleteOrdersDishes";
    public static final String GET_WITH_DISHES = "Order.getWithDishes";

    /*user which has made the Order*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    /*restaurant in which Order was made*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    /*list of OrdersDishes elements that represents
     *many-to-many relationship between Orders and Dishes
     * with one additional field - quantity of Dish in order*/
    @OneToMany(fetch = FetchType.LAZY ,mappedBy = "order")
    private List<OrdersDishes> dishes;

    /*Date and Time when Order made*/
    @Column(name = "date_time" , nullable = false)
    @DateTimeFormat(pattern = DateTimeUtil.DATE_TIME_PATTERN)
    private LocalDateTime dateTime;

    /*Status of order*/
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    /*Total price of order*/
    @Column(name = "total_price",nullable = false)
    private double totalPrice;

    public Order() {
    }

    public Order(User user, Restaurant restaurant, LocalDateTime dateTime) {
        this.user = user;
        this.restaurant = restaurant;
        this.dateTime = dateTime;
        this.status = Status.ACCEPTED;
    }

    public Order(User user, Restaurant restaurant, List<OrdersDishes> dishes, LocalDateTime dateTime, Status status) {
        this.user = user;
        this.restaurant = restaurant;
        this.dishes = dishes;
        this.dateTime = dateTime;
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }


    /*getter and setter of List<OrdersDishes> dishes returns and accepts
    * Map<Dish,Integer> for comfortable use in other layers*/

    /*method returns from List<OrdersDishes> orders map with keys - dishes and values -
    * quantities of this dish in the order  */
    public Map<Dish, Integer> getDishes() {
        Map<Dish,Integer> result = new TreeMap<>(ComparatorUtil.dishComparator);
        for (OrdersDishes dish : dishes){
            result.put(dish.getDish(), dish.getDishQuantity());
        }
        return result;
    }

    /*method save map wit keys - dishes and values
    *- quantities of this dish in the order into  List<OrdersDishes> dishes*/
    public void setDishes(Map<Dish, Integer> dishes) {
        List<OrdersDishes> result = new ArrayList<>();
        for (Map.Entry<Dish,Integer> dish : dishes.entrySet()){
            OrdersDishes orderDishes = new OrdersDishes();
            orderDishes.setOrder(this);
            orderDishes.setDish(dish.getKey());
            orderDishes.setDishQuantity(dish.getValue());
            result.add(orderDishes);
        }
        this.dishes = result;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Order{" +
                "dateTime=" + dateTime +
                '}';
    }
}
