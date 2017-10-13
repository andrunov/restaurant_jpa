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
import ru.agorbunov.restaurant.model.Dish;
import ru.agorbunov.restaurant.model.MenuList;
import ru.agorbunov.restaurant.model.Order;
import ru.agorbunov.restaurant.repository.DishRepository;
import ru.agorbunov.restaurant.util.ComparatorUtil;
import ru.agorbunov.restaurant.util.DateTimeUtil;

import javax.sql.DataSource;
import java.util.*;

/**
 * Dish-entities repository by Java DataBase Connectivity
 */
@Repository
@Transactional(readOnly = true)
public class JdbcDishRepositoryImpl<T> implements DishRepository {
    private static final BeanPropertyRowMapper<Dish> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Dish.class);
    private static final BeanPropertyRowMapper<MenuList> MENU_LIST_ROW_MAPPER = BeanPropertyRowMapper.newInstance(MenuList.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SimpleJdbcInsert insertDish;

    @Autowired
    public JdbcDishRepositoryImpl(DataSource dataSource) {
        this.insertDish = new SimpleJdbcInsert(dataSource)
                .withTableName("dishes")
                .usingGeneratedKeyColumns("id");
    }

    /*save dish in database, menulistId in parameters is Id of menu list to which the dish is belong*/
    @Override
    @Transactional
    public Dish save(Dish dish, int menulistId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", dish.getId())
                .addValue("menu_list_id", menulistId)
                .addValue("description", dish.getDescription())
                .addValue("price", dish.getPrice());
        if (dish.isNew()) {
            Number newKey = insertDish.executeAndReturnKey(map);
            dish.setId(newKey.intValue());
        } else {
            if(namedParameterJdbcTemplate.update("UPDATE dishes SET description=:description, price=:price WHERE id=:id AND menu_list_id=:menu_list_id", map)==0){
                return null;
            }
        }
        return dish;
    }


    /*get dish from database by Id, menulistId in parameters is Id of menu list to which the dish is belong*/
    @Override
    public Dish get(int id, int menuListId) {
        List<Dish> dishes = jdbcTemplate.query("SELECT * FROM dishes WHERE id=? AND menu_list_id=?", ROW_MAPPER, id,menuListId);
        return DataAccessUtils.singleResult(dishes);

    }

    /*get dish from database bi Id with collection of orders which contains the dish,
    * menulistId in parameters is Id of menu list to which the dish is belong*/    @Override
    public Dish getWithOrders(int id, int menuListId) {
        List<Dish> dishes = jdbcTemplate.query("SELECT * FROM dishes WHERE id=? AND menu_list_id=?", ROW_MAPPER, id,menuListId);
        Dish result = DataAccessUtils.singleResult(dishes);
        return setOrders(result);
    }

    /*get all dishes from database that belongs to menuList with Id pass as parameter */
    @Override
    public List<Dish> getByMenuList(int menuListId) {
        List<Dish> dishes = jdbcTemplate.query("SELECT * FROM dishes WHERE menu_list_id=?", ROW_MAPPER, menuListId);
        for (Dish dish : dishes) {
            setMenuList(dish);
        }
        return dishes;
    }

    /*get all dishes from database that belongs to order with Id pass as parameter */
    @Override
    public Map<Dish,Integer> getByOrder(int orderId) {
        List<Map<String,Object>> results = jdbcTemplate.queryForList("SELECT d.* , od.dish_quantity FROM orders_dishes AS od JOIN dishes as d ON d.id = od.dish_id WHERE od.order_id=? ",orderId);
        Map<Dish,Integer> dishMap = new TreeMap<>(ComparatorUtil.dishComparator);
        for (Map row : results){
            Dish dish = new Dish();
            dish.setId((Integer)row.get("id"));
            dish.setDescription((String) row.get("description"));
            dish.setPrice((Double) row.get("price"));
            Integer dishQuantity = (Integer)row.get("dish_quantity");
            dishMap.put(dish,dishQuantity);
        }
        return dishMap;
    }

    /*delete references to dishes from order in database, dish Id and order Id pass in parameters */
    @Override
    @Transactional
    public boolean deleteFromOrder(int id, int orderId) {
        return jdbcTemplate.update("DELETE FROM orders_dishes WHERE dish_id=? AND order_id=?", id, orderId) != 0;
    }

    /*delete dish from database by dish Id */
    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM dishes WHERE id=?", id) != 0;
    }

    /*get all dishes from database*/
    @Override
    public List<Dish> getAll() {
        return jdbcTemplate.query("SELECT * FROM dishes", ROW_MAPPER);
    }

    /*get all orders from database that contains the dish pass in parameter*/
    private Dish setOrders(Dish d) {
        if (d != null) {
            List<Map<String,Object>> results = jdbcTemplate.queryForList("SELECT o.*, od.dish_quantity FROM orders AS o JOIN orders_dishes AS od ON o.id = od.order_id WHERE od.dish_id=?", d.getId());
            Map<Order,Integer> orderMap = new HashMap<>();
            for (Map row : results){
                Order order = new Order();
                order.setId((Integer)row.get("id"));
                order.setDateTime(DateTimeUtil.parseLocalDateTime(row.get("date_time")
                                  .toString().substring(0,16),
                                  DateTimeUtil.DATE_TIME_FORMATTER));
                Integer dishQuantity = (Integer)row.get("dish_quantity");
                orderMap.put(order,dishQuantity);
            }
            d.setOrders(orderMap);
        }
        return d;
    }

    /*get menuLists from database to which the dish is belong
    *(dish pass in parameter) and set it to dish */
    private Dish setMenuList(Dish d){
        if (d != null) {
            List<MenuList> dishes = jdbcTemplate.query("SELECT m.id, m.date_time FROM menu_lists AS m JOIN dishes AS d ON m.id = d.menu_list_id WHERE d.id=?",
                    MENU_LIST_ROW_MAPPER, d.getId());
            d.setMenuList(DataAccessUtils.singleResult(dishes));
        }
        return d;
    }
}
