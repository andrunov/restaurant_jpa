package ru.agorbunov.restaurant.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.agorbunov.restaurant.model.User;
import ru.agorbunov.restaurant.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * User-entities repository by Java Persistence API
 */
@Repository
@Transactional(readOnly = true)
public class JpaUserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    /*save user in database*/
    @Override
    @Transactional
    public User save(User user) {
        if (user.isNew()){
            em.persist(user);
            return user;
        }else {
            return em.merge(user);
        }
    }

    /*delete user from database by Id */
    @Override
    @Transactional
    public boolean delete(int id) {
        return em.createNamedQuery(User.DELETE)
                .setParameter("id", id)
                .executeUpdate() !=0;
    }

    /*get all users from database*/
    @Override
    public List<User> getAll() {
        return em.createNamedQuery(User.GET_ALL, User.class).getResultList();
    }

    /*get user from database by Id*/
    @Override
    public User get(int id) {
        return em.find(User.class, id);
    }

    /*get user from database by Id with collection of
    *orders were made by the user*/
    @Override
    public User getWithOrders(int id) {
        return (User)em.createNamedQuery(User.GET_WITH_ORDERS)
                .setParameter("id",id)
                .getSingleResult();
    }

    /*get user from database by email*/
    @Override
    public User getByEmail(String email) {
        List<User> users = em.createNamedQuery(User.BY_EMAIL, User.class).setParameter(1, email).getResultList();
        return DataAccessUtils.singleResult(users);
    }

    /*get total amount of user's orders from database
    * and save it to database to users's field "totalOrdersAmount" */
    @Transactional
    @Override
    public void accountAndSaveTotalOrdersAmount(int id) {
        em.createNamedQuery(User.ACCOUNT_AND_SAVE_TOTAL_ORDERS_AMOUNT)
                .setParameter(1,id)
                .setParameter(2,id)
                .executeUpdate();
    }
}
