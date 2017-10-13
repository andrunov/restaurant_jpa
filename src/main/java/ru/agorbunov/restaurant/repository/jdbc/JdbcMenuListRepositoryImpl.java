package ru.agorbunov.restaurant.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.agorbunov.restaurant.Profiles;
import ru.agorbunov.restaurant.model.Dish;
import ru.agorbunov.restaurant.model.MenuList;
import ru.agorbunov.restaurant.repository.MenuListRepository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * MenuList-entities repository by Java DataBase Connectivity
 * announced as abstract, consist inner classes for
 * customise for different databases
 */
//// TODO: 17.05.2017 remove profiles before production
@Transactional(readOnly = true)
public abstract class JdbcMenuListRepositoryImpl<T> implements MenuListRepository {
    private static final BeanPropertyRowMapper<MenuList> ROW_MAPPER = BeanPropertyRowMapper.newInstance(MenuList.class);
    private static final BeanPropertyRowMapper<Dish> DISH_ROW_MAPPER = BeanPropertyRowMapper.newInstance(Dish.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SimpleJdbcInsert insertMenuList;

    /*method to specify behaviour different database with LocalDateTime*/
    protected abstract T toDbDateTime(LocalDateTime ldt);


    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.insertMenuList = new SimpleJdbcInsert(dataSource)
                .withTableName("menu_lists")
                .usingGeneratedKeyColumns("id");
    }

    /*Customise repository for Postgres*/
    @Repository
    @Profile(Profiles.POSTGRES)
    public static class Java8JdbcMenuListRepositoryImpl extends JdbcMenuListRepositoryImpl<LocalDateTime> {
        @Override
        protected LocalDateTime toDbDateTime(LocalDateTime ldt) {
            return ldt;
        }
    }

    /*Customise repository for HSQLDB*/
    @Repository
    @Profile(Profiles.HSQLDB)
    public static class TimestampJdbcMenuListRepositoryImpl extends JdbcMenuListRepositoryImpl<Timestamp> {
        @Override
        protected Timestamp toDbDateTime(LocalDateTime ldt) {
            return Timestamp.valueOf(ldt);
        }
    }

    /*save menuList in database, restaurantId in parameters is Id
    *of restaurant to which the menuList is belong*/
    @Override
    @Transactional
    public MenuList save(MenuList menuList, int restaurantId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", menuList.getId())
                .addValue("restaurant_id", restaurantId)
                .addValue("description", menuList.getDescription())
                .addValue("date_time", toDbDateTime(menuList.getDateTime()))
                .addValue("enabled", menuList.isEnabled());

        if (menuList.isNew()) {
            Number newKey = insertMenuList.executeAndReturnKey(map);
            menuList.setId(newKey.intValue());
        } else {
            if(namedParameterJdbcTemplate.update("UPDATE menu_lists SET date_time=:date_time, description=:description, enabled=:enabled WHERE id=:id AND restaurant_id=:restaurant_id", map)==0){
                return null;
            }
        }
        return menuList;
    }

    /*get menuList from database by Id, restaurantId in parameters is Id
    *of restaurant to which the menuList is belong*/
    @Override
    public MenuList get(int id, int restaurantId) {
        List<MenuList> menuLists = jdbcTemplate.query("SELECT * FROM menu_lists WHERE id=? AND restaurant_id=?", ROW_MAPPER, id,restaurantId);
        return DataAccessUtils.singleResult(menuLists);
    }

    /*get menuList from database by Id with collection of dishes which the menuList is have,
    * restaurantId in parameters is Id of restaurant to which the menuList is belong*/
    @Override
    public MenuList getWithDishes(int id, int restaurantId) {
        List<MenuList> menuLists = jdbcTemplate.query("SELECT * FROM menu_lists WHERE id=? AND restaurant_id=?", ROW_MAPPER, id,restaurantId);
        MenuList result = DataAccessUtils.singleResult(menuLists);
        return setDishes(result);
    }

    /*delete menuList from database by Id */
    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM menu_lists WHERE id=?", id) != 0;
    }

    /*get all menuLists from database*/
    @Override
    public List<MenuList> getAll() {
        return jdbcTemplate.query("SELECT * FROM menu_lists ORDER BY date_time DESC", ROW_MAPPER);
    }

    /*get all menuLists from database that belongs to restaurant with Id pass as parameter */
    @Override
    public List<MenuList> getByRestaurant(int restaurantId) {
        return jdbcTemplate.query("SELECT * FROM menu_lists WHERE restaurant_id=? ORDER BY date_time DESC", ROW_MAPPER, restaurantId);
    }

    /*get dishes from database which the menuList is have
    *(menuList pass in parameter) and set it to dish as List<Dish> */
    private MenuList setDishes(MenuList m) {
        if (m != null) {
            List<Dish> dishes = jdbcTemplate.query("SELECT * FROM dishes  WHERE menu_list_id=?",
                    DISH_ROW_MAPPER, m.getId());
            m.setDishList(dishes);
        }
        return m;
    }

    /*get all menuLists from database that belongs to restaurant with Id pass as 1st parameter
     * and according to status pass as 2nd parameter*/
    @Override
    public List<MenuList> getByRestaurantAndEnabled(int restaurantId, boolean enabled) {
        return jdbcTemplate.query("SELECT * FROM menu_lists WHERE restaurant_id=? AND enabled=? ORDER BY date_time DESC", ROW_MAPPER, restaurantId, enabled);
    }

    /*get menuList from database by dish Id, belongs to this menu list*/
    @Transactional
    @Override
    public MenuList getByDish(int dishId) {
        List<MenuList> menuLists = jdbcTemplate.query("SELECT ml.* FROM MENU_LISTS as ml WHERE ml.ID IN (SELECT d.MENU_LIST_ID FROM DISHES AS d WHERE d.ID=?)", ROW_MAPPER, dishId);
        MenuList menuList =  DataAccessUtils.singleResult(menuLists);
        setDishes(menuList);
        return menuList != null && containsDish(menuList,dishId) ? menuList : null;
    }

    /*check that menuList (1st parameter contains dish (dishId pass as 2nd parameter*/
    private boolean containsDish(MenuList menuList, int dishId){
        List<Dish> dishes = menuList.getDishList();
        if ((dishes==null) && (dishes.size()==0)) return  false;
        for (Dish dish : dishes){
            if (dish.getId() == dishId) return true;
        }
        return  false;
    }
}
