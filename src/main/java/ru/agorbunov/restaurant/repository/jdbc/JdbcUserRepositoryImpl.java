package ru.agorbunov.restaurant.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.agorbunov.restaurant.model.Order;
import ru.agorbunov.restaurant.model.Role;
import ru.agorbunov.restaurant.model.User;
import ru.agorbunov.restaurant.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * User-entities repository by Java DataBase Connectivity
 */
@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private static final BeanPropertyRowMapper<Order> ORDER_ROW_MAPPER = BeanPropertyRowMapper.newInstance(Order.class);
    private static final RowMapper<Role> ROLE_ROW_MAPPER = (rs, rowNum) -> Role.valueOf(rs.getString("role"));

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
    }

    /*save user in database*/
    @Override
    @Transactional
    public User save(User user) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("name", user.getName())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("enabled", user.isEnabled())
                .addValue("totalOrdersAmount", user.getTotalOrdersAmount());

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(map);
            user.setId(newKey.intValue());
            insertRoles(user);
        } else {
            deleteRoles(user);
            insertRoles(user);
            namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, password=:password, email=:email, enabled=:enabled, totalOrdersAmount=:totalOrdersAmount WHERE id=:id", map);
        }
        return user;
    }

    /*get user from database by Id*/
    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        return setRoles(DataAccessUtils.singleResult(users));
    }

    /*get user from database by Id with collection of
    *orders were made by the user*/
    @Override
    public User getWithOrders(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        User result = DataAccessUtils.singleResult(users);
        setRoles(result);
        setOrders(result);
        return result;
    }

    /*delete user from database by Id */
    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    /*get all users from database*/
    @Override
    public List<User> getAll() {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM roles");
        Map<Integer, Set<Role>> map = new HashMap<>();
        while (rowSet.next()) {
            Set<Role> roles = map.computeIfAbsent(rowSet.getInt("user_id"), userId -> EnumSet.noneOf(Role.class));
            roles.add(Role.valueOf(rowSet.getString("role")));
        }
        List<User> users = jdbcTemplate.query("SELECT * FROM users", ROW_MAPPER);
        users.forEach(u -> u.setRoles(map.get(u.getId())));
        return users;
    }

    /*get roles of user and saves them ti database*/
    private void insertRoles(User u) {
        Set<Role> roles = u.getRoles();
        Iterator<Role> iterator = roles.iterator();

        jdbcTemplate.batchUpdate("INSERT INTO roles (user_id, role) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, u.getId());
                        ps.setString(2, iterator.next().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return roles.size();
                    }
                });
    }

    /*delete roles of user from database*/
    private void deleteRoles(User u) {
        jdbcTemplate.update("DELETE FROM roles WHERE user_id=?", u.getId());
    }

    /*gets roles from database which belongs to user
    * and set them to the user as Set<Role>*/
    private User setRoles(User u) {
        if (u != null) {
            List<Role> roles = jdbcTemplate.query("SELECT role FROM roles  WHERE user_id=?",
                                                    ROLE_ROW_MAPPER, u.getId());
            u.setRoles(EnumSet.copyOf(roles));
        }
        return u;
    }

    /*gets orders from database which belongs to user
    * and set them to the user as List<Order>*/
    private User setOrders(User u) {
        if (u != null) {
            List<Order> orders = jdbcTemplate.query("SELECT * FROM orders  WHERE user_id=?",
                    ORDER_ROW_MAPPER, u.getId());
            u.setOrders(orders);
        }
        return u;
    }

    /*get user from database by email*/
    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return setRoles(DataAccessUtils.singleResult(users));
    }

    /*get total amount of user's orders from database
    * and save it to database to users's field "totalOrdersAmount" */
    @Transactional
    @Override
    public void accountAndSaveTotalOrdersAmount(int id) {
        jdbcTemplate.update("UPDATE USERS SET totalOrdersAmount=(SELECT SUM(TOTAL_PRICE) FROM orders  WHERE user_id=?) WHERE id=?",id,id);
    }
}
