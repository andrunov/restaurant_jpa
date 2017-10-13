package ru.agorbunov.restaurant.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.agorbunov.restaurant.Profiles;
import ru.agorbunov.restaurant.model.Dish;
import ru.agorbunov.restaurant.model.Order;
import ru.agorbunov.restaurant.model.Restaurant;
import ru.agorbunov.restaurant.model.User;
import ru.agorbunov.restaurant.repository.OrderRepository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Order-entities repository by Java DataBase Connectivity
 * announced as abstract, consist inner classes for
 * customise for different databases
 */
//// TODO: 24.02.2017 remove profiles before production
@Transactional(readOnly = true)
public abstract class JdbcOrderRepositoryImpl<T> implements OrderRepository {
    private static final BeanPropertyRowMapper<Order> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Order.class);
    private static final BeanPropertyRowMapper<Restaurant> RESTAURANT_ROW_MAPPER = BeanPropertyRowMapper.newInstance(Restaurant.class);
    private static final BeanPropertyRowMapper<User> USER_ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SimpleJdbcInsert insertOrder;

    /*method to specify behaviour different database with LocalDateTime*/
    protected abstract T toDbDateTime(LocalDateTime ldt);

    @Autowired
    private void setDataSource(DataSource dataSource) {
        this.insertOrder = new SimpleJdbcInsert(dataSource)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }

    /*Customise repository for Postgres*/
    @Repository
    @Profile(Profiles.POSTGRES)
    public static class Java8JdbcOrderRepositoryImpl extends JdbcOrderRepositoryImpl<LocalDateTime> {
        @Override
        protected LocalDateTime toDbDateTime(LocalDateTime ldt) {
            return ldt;
        }
    }

    /*Customise repository for HSQLDB*/
    @Repository
    @Profile(Profiles.HSQLDB)
    public static class TimestampJdbcOrderRepositoryImpl extends JdbcOrderRepositoryImpl<Timestamp> {
        @Override
        protected Timestamp toDbDateTime(LocalDateTime ldt) {
            return Timestamp.valueOf(ldt);
        }
    }

    /*save order in database if it is new entity and update if it is exist,
    *int[] dishIds - Ids of dishes, int[] dishQuantityValues - dishes quantities,
    *each dishId from first arr matches its quantity from second arr, arrays must have equal size
    *userId and restaurantId in parameters is Ids of user and restaurant to which the order is belong*/
    @Override
    @Transactional
    public Order save(Order order, int userId, int restaurantId, int[] dishIds, int[] dishQuantityValues) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", order.getId())
                .addValue("status", order.getStatus().toString())
                .addValue("user_id", userId)
                .addValue("restaurant_id", restaurantId)
                .addValue("date_time", toDbDateTime(order.getDateTime()))
                .addValue("total_price",order.getTotalPrice());
        if (order.isNew()) {
            Number newKey = insertOrder.executeAndReturnKey(map);
            order.setId(newKey.intValue());
            insertDishes(order.getId(), dishIds, dishQuantityValues);
        } else {
            if(namedParameterJdbcTemplate.update("UPDATE orders SET date_time=:date_time, status=:status, total_price=:total_price WHERE id=:id AND user_id=:user_id AND restaurant_id=:restaurant_id", map)==0){
                return null;
            }else {
                deleteDishes(order.getId());
                insertDishes(order.getId(), dishIds, dishQuantityValues);
            }
        }

        return order;
    }

    /*save order in database if it is new entity and update if it is exist,
    *userId and restaurantId in parameters is Ids of user and restaurant to which the order is belong,
    *if order is already exist and have collections of dishes they not erase in database*/
    @Override
    @Transactional
    public Order save(Order order, int userId, int restaurantId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", order.getId())
                .addValue("status", order.getStatus().toString())
                .addValue("user_id", userId)
                .addValue("restaurant_id", restaurantId)
                .addValue("date_time", toDbDateTime(order.getDateTime()));

        if (order.isNew()) {
            Number newKey = insertOrder.executeAndReturnKey(map);
            order.setId(newKey.intValue());
        } else {
            if(namedParameterJdbcTemplate.update("UPDATE orders SET date_time=:date_time, status=:status WHERE id=:id AND user_id=:user_id AND restaurant_id=:restaurant_id", map)==0){
                return null;
            }
        }

        return order;
    }

    /*delete dishes which belongs to order from database, Id of order pass in parameter */
    private boolean deleteDishes(int orderId){
        return jdbcTemplate.update("DELETE FROM orders_dishes WHERE order_id=?", orderId) != 0;

    }

    /*saves order's dishes and their quantities to database,
    * int[] dishIds - Ids of dishes, int[] dishQuantityValues - dishes quantities,
    * each dishId from first arr matches its quantity from second arr, arrays must have equal size*/
    private void insertDishes(int orderId, int[] dishIds, int[] dishQuantityValues) {
        jdbcTemplate.batchUpdate("INSERT INTO orders_dishes (order_id, dish_id, dish_quantity) VALUES (?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setInt(1, orderId);
                            ps.setInt(2, dishIds[i]);
                            ps.setInt(3, dishQuantityValues[i]);
                    }

                    @Override
                    public int getBatchSize() {
                        return dishIds.length;
                    }
                });
    }

    /*get order from database by Id, userId and restaurantId in parameters is Ids of
    *user and restaurant to which the order is belong*/
    @Override
    public Order get(int id, int userId, int restaurantId) {
        List<Order> orders = jdbcTemplate.query("SELECT * FROM orders WHERE id=? AND user_id=? AND restaurant_id=?", ROW_MAPPER, id,userId,restaurantId);
        return DataAccessUtils.singleResult(orders);
    }

    /*get order from database by Id with collections of dishes which the order is have ,
    *userId and restaurantId in parameters is Ids of
    *user and restaurant to which the order is belong*/
    @Override
    public Order getWithDishes(int id, int userId, int restaurantId) {
        List<Order> orders = jdbcTemplate.query("SELECT * FROM orders WHERE id=? AND user_id=? AND restaurant_id=?", ROW_MAPPER, id,userId,restaurantId);
        Order result = DataAccessUtils.singleResult(orders);
        return setDishes(result);
    }

    /*delete order from database by Id */
    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM orders WHERE id=?", id) != 0;
    }

    /*get all orders from database*/
    @Override
    public List<Order> getAll() {
        return jdbcTemplate.query("SELECT * FROM orders ORDER BY date_time DESC ", ROW_MAPPER);
    }

    /*get all orders from database that belongs to user with Id pass as parameter */
    @Override
    public List<Order> getByUser(int userId) {
        List<Order> result = jdbcTemplate.query("SELECT * FROM orders WHERE user_id=? ORDER BY date_time DESC ", ROW_MAPPER,userId);
        for (Order order : result) {
            setRestaurant(order);
        }
        return result;
    }

    /*get all orders from database that belongs to user with Id pass as 1st parameter
   * and with status pass as 2nd parameter */
    @Override
    public List<Order> getByUserAndStatus(int userId, String status) {
        List<Order> result = jdbcTemplate.query("SELECT * FROM orders WHERE user_id=? AND status=? ORDER BY date_time DESC ", ROW_MAPPER,userId,status);
        for (Order order : result) {
            setRestaurant(order);
        }
        return result;
    }

    /*get all orders from database that belongs to user with Id pass as 1st parameter
    * and which made on Date  pass as 2nd parameter */
    @Override
    public List<Order> getByUserAndDate(int userId, LocalDateTime localDateTime) {
        LocalDateTime beginDate = localDateTime.toLocalDate().atStartOfDay();
        LocalDateTime endDate = beginDate.plusHours(23).plusMinutes(59).minusSeconds(59).plusNanos(999999999);
        List<Order> result = jdbcTemplate.query("SELECT * FROM orders WHERE user_id=? AND date_time>=? AND date_time<=? ORDER BY date_time DESC ", ROW_MAPPER,userId,toDbDateTime(beginDate),toDbDateTime(endDate));
        for (Order order : result) {
            setRestaurant(order);
        }
        return result;
    }

    /*get all orders from database that belongs to user with Id pass as 1st parameter
    * and with status pass as 2nd parameter and which made on Date  pass as 3rd parameter */
    @Override
    public List<Order> getByUserAndStatusAndDate(int userId, String status, LocalDateTime localDateTime) {
        LocalDateTime beginDate = localDateTime.toLocalDate().atStartOfDay();
        LocalDateTime endDate = beginDate.plusHours(23).plusMinutes(59).minusSeconds(59).plusNanos(999999999);
        List<Order> result = jdbcTemplate.query("SELECT * FROM orders WHERE user_id=? AND status=?  AND date_time>=? AND date_time<=? ORDER BY date_time DESC ", ROW_MAPPER,userId,status,toDbDateTime(beginDate),toDbDateTime(endDate));
        for (Order order : result) {
            setRestaurant(order);
        }
        return result;
    }

    /*get all orders from database that belongs to dish with Id pass as parameter */
    @Override
    public List<Order> getByDish(int dishId) {
        List<Order> result = jdbcTemplate.query("SELECT o.* FROM orders AS o JOIN orders_dishes AS od ON o.id = od.order_id WHERE od.dish_id=? ORDER BY date_time DESC ", ROW_MAPPER,dishId);
        for (Order order : result) {
            setUser(order);
        }
        return result;
    }

    /*get all orders from database that belongs to dish with Id pass as parameter *
    * and with status pass as 2nd parameter */
    @Override
    public List<Order> getByDishAndStatus(int dishId, String status) {
        List<Order> result = jdbcTemplate.query("SELECT o.* FROM orders AS o JOIN orders_dishes AS od ON o.id = od.order_id WHERE od.dish_id=? AND status=? ORDER BY date_time DESC ", ROW_MAPPER,dishId,status);
        for (Order order : result) {
            setUser(order);
        }
        return result;
    }

    /*get all orders from database that belongs to dish with Id pass as parameter *
     * and which made on Date  pass as 2nd parameter */
    @Override
    public List<Order> getByDishAndDate(int dishId, LocalDateTime localDateTime) {
        LocalDateTime beginDate = localDateTime.toLocalDate().atStartOfDay();
        LocalDateTime endDate = beginDate.plusHours(23).plusMinutes(59).minusSeconds(59).plusNanos(999999999);
        List<Order> result = jdbcTemplate.query("SELECT o.* FROM orders AS o JOIN orders_dishes AS od ON o.id = od.order_id WHERE od.dish_id=? AND date_time>=? AND date_time<=? ORDER BY date_time DESC  ", ROW_MAPPER, dishId, toDbDateTime(beginDate),toDbDateTime(endDate));
        for (Order order : result) {
            setUser(order);
        }
        return result;
    }

    /*get all orders from database that belongs to dish with Id pass as parameter *
     * and with status pass as 2nd parameter and which made on Date  pass as 3rd parameter */
    @Override
    public List<Order> getByDishAndStatusAndDate(int dishId, String status, LocalDateTime localDateTime) {
        LocalDateTime beginDate = localDateTime.toLocalDate().atStartOfDay();
        LocalDateTime endDate = beginDate.plusHours(23).plusMinutes(59).minusSeconds(59).plusNanos(999999999);
        List<Order> result = jdbcTemplate.query("SELECT o.* FROM orders AS o JOIN orders_dishes AS od ON o.id = od.order_id WHERE od.dish_id=? AND  status=? AND date_time>=? AND date_time<=? ORDER BY date_time DESC", ROW_MAPPER, dishId, status, toDbDateTime(beginDate),toDbDateTime(endDate));
        for (Order order : result) {
            setUser(order);
        }
        return result;
    }

    /*get order's dishes with their quantities and set them to the order*/
    private Order setDishes(Order o) {
        if (o != null) {
            List<Map<String,Object>> results = jdbcTemplate.queryForList("SELECT d.* , od.dish_quantity FROM orders_dishes AS od JOIN dishes as d ON d.id = od.dish_id WHERE od.order_id=? ",o.getId());
            Map<Dish,Integer> dishMap = new HashMap<>();
            for (Map row : results){
                Dish dish = new Dish();
                dish.setId((Integer)row.get("id"));
                dish.setDescription((String) row.get("description"));
                dish.setPrice((Double) row.get("price"));
                Integer dishQuantity = (Integer)row.get("dish_quantity");
                dishMap.put(dish,dishQuantity);
            }
            o.setDishes(dishMap);
        }
        return o;
    }

    /*get restaurant where order was made and set it to order*/
    private Order setRestaurant(Order o){
        if (o != null) {
            List<Restaurant> restaurants = jdbcTemplate.query("SELECT r.id, r.name, r.address FROM restaurants AS r JOIN orders AS o ON r.id = o.restaurant_id WHERE o.id=?",
                    RESTAURANT_ROW_MAPPER, o.getId());
            o.setRestaurant(DataAccessUtils.singleResult(restaurants));
        }
        return o;
    }

    /*get user by which order was made and set it to order*/
    private Order setUser(Order o){
        if (o != null) {
            List<User> users = jdbcTemplate.query("SELECT u.id, u.name, u.email FROM users AS u JOIN orders AS o ON u.id = o.user_id WHERE o.id=?",
                    USER_ROW_MAPPER, o.getId());
            o.setUser(DataAccessUtils.singleResult(users));
        }
        return o;
    }
}
