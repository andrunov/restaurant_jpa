package ru.agorbunov.restaurant.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Class represents users
 */
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SuppressWarnings("JpaQlInspection")
@NamedNativeQuery(name = User.ACCOUNT_AND_SAVE_TOTAL_ORDERS_AMOUNT, query = "UPDATE USERS SET totalOrdersAmount=(SELECT SUM(TOTAL_PRICE) FROM orders  WHERE user_id=?) WHERE id=?")
@NamedQueries({
        @NamedQuery(name = User.GET_ALL, query = "SELECT u from User u"),
        @NamedQuery(name = User.GET_WITH_ORDERS, query = "SELECT u from User u JOIN FETCH u.orders WHERE u.id = :id"),
        @NamedQuery(name = User.BY_EMAIL, query = "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email=?1"),
        @NamedQuery(name = User.DELETE, query = "DELETE FROM User u WHERE u.id=:id")
})
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    public static final String GET_ALL = "User.getAll";
    public static final String GET_WITH_ORDERS = "User.getWithOrders";
    public static final String DELETE = "User.delete";
    public static final String BY_EMAIL = "User.getByEmail";
    public static final String ACCOUNT_AND_SAVE_TOTAL_ORDERS_AMOUNT = "User.accountAndSaveTotalOrdersAmount";


    /*user's name*/
    @Column(nullable = false)
    private String name;

    /*users e-mail*/
    @Column(nullable = false, unique = true)
    @Email
    private String email;

    /*users password*/
    @Column(nullable = false)
    private String password;

    /*enabled-disabled*/
    @Column(name = "enabled", nullable = false, columnDefinition = "bool default true")
    private boolean enabled;

    /*total amount of users orders*/
    @Column(name = "totalOrdersAmount")
    private double totalOrdersAmount;

    /*users roles*/
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    /*orders has made by the user */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Order> orders;


    public User() {
    }

    public User(String name, String email, String password, Role role, Role... roles) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.enabled = true;
        this.roles = EnumSet.of(role, roles);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public double getTotalOrdersAmount() {
        return totalOrdersAmount;
    }

    public void setTotalOrdersAmount(double totalOrdersAmount) {
        this.totalOrdersAmount = totalOrdersAmount;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
