package ru.agorbunov.restaurant.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.agorbunov.restaurant.model.MenuList;
import ru.agorbunov.restaurant.model.Order;
import ru.agorbunov.restaurant.model.Restaurant;
import ru.agorbunov.restaurant.repository.RestaurantRepository;

import javax.sql.DataSource;
import java.util.List;

/**
 * Restaurant-entities repository by Java DataBase Connectivity
 */
@Repository
@Transactional(readOnly = true)
public class JdbcRestaurantRepositoryImpl implements RestaurantRepository {
    private static final BeanPropertyRowMapper<Restaurant> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Restaurant.class);
    private static final BeanPropertyRowMapper<MenuList> MENU_LIST_ROW_MAPPER = BeanPropertyRowMapper.newInstance(MenuList.class);
    private static final BeanPropertyRowMapper<Order> ORDER_ROW_MAPPER = BeanPropertyRowMapper.newInstance(Order.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SimpleJdbcInsert insertRestaurant;

    @Autowired
    public JdbcRestaurantRepositoryImpl(DataSource dataSource) {
        this.insertRestaurant = new SimpleJdbcInsert(dataSource)
                .withTableName("restaurants")
                .usingGeneratedKeyColumns("id");
    }

    /*save restaurant in database*/
    @Override
    @Transactional
    public Restaurant save(Restaurant restaurant) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", restaurant.getId())
                .addValue("name", restaurant.getName())
                .addValue("address", restaurant.getAddress());

        if (restaurant.isNew()) {
            Number newKey = insertRestaurant.executeAndReturnKey(map);
            restaurant.setId(newKey.intValue());
        } else {
            namedParameterJdbcTemplate.update(
                    "UPDATE restaurants SET name=:name, address=:address WHERE id=:id", map);
        }
        return restaurant;
    }

    /*get restaurant from database by Id*/
    @Override
    public Restaurant get(int id) {
        List<Restaurant> restaurants = jdbcTemplate.query("SELECT * FROM restaurants WHERE id=?", ROW_MAPPER, id);
        return DataAccessUtils.singleResult(restaurants);
    }

    /*get restaurant from database by Id with collection of
    *menuLists were issued by the restaurant*/
    @Override
    public Restaurant getWithMenuLists(int id) {
        List<Restaurant> restaurants = jdbcTemplate.query("SELECT * FROM restaurants WHERE id=?", ROW_MAPPER, id);
        Restaurant result = DataAccessUtils.singleResult(restaurants);
        setOrders(result);
        return setMenuLists(result);
    }

    /*delete restaurant from database by Id */
    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM restaurants WHERE id=?", id) != 0;
    }

    /*get all restaurants from database*/
    @Override
    public List<Restaurant> getAll() {
        return jdbcTemplate.query("SELECT * FROM restaurants", ROW_MAPPER);
    }

    /*get menuLists from database which were issued by the restaurant
    * and set them to Restaurant-entity as List<MenuList>*/
    private Restaurant setMenuLists(Restaurant r) {
        if (r != null) {
            List<MenuList> menuLists = jdbcTemplate.query("SELECT * FROM menu_lists  WHERE restaurant_id=?",
                    MENU_LIST_ROW_MAPPER, r.getId());
            r.setMenuLists(menuLists);
        }
        return r;
    }

    /*get orders from database which were made in the restaurant
    * and set them to Restaurant-entity as List<Order>*/
    private Restaurant setOrders(Restaurant r) {
        if (r != null) {
            List<Order> orders = jdbcTemplate.query("SELECT * FROM orders  WHERE restaurant_id=?",
                    ORDER_ROW_MAPPER, r.getId());
            r.setOrders(orders);
        }
        return r;
    }
}
