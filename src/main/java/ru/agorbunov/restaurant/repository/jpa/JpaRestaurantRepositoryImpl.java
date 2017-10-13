package ru.agorbunov.restaurant.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.agorbunov.restaurant.model.Order;
import ru.agorbunov.restaurant.model.Restaurant;
import ru.agorbunov.restaurant.repository.RestaurantRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Restaurant-entities repository by Java Persistence API
 */
@Repository
@Transactional(readOnly = true)
public class JpaRestaurantRepositoryImpl implements RestaurantRepository {

    @PersistenceContext
    private EntityManager em;

    /*save restaurant in database*/
    @Override
    @Transactional
    public Restaurant save(Restaurant restaurant) {
        if (restaurant.isNew()){
            em.persist(restaurant);
            return restaurant;
        }else {
            return em.merge(restaurant);
        }
    }

    /*delete restaurant from database by Id */
    @Override
    @Transactional
    public boolean delete(int id) {
        return em.createNamedQuery(Restaurant.DELETE)
                .setParameter("id", id)
                .executeUpdate() !=0;
    }

    /*get all restaurants from database*/
    @Override
    public List<Restaurant> getAll() {
        return em.createNamedQuery(Restaurant.GET_ALL, Restaurant.class).getResultList();
    }

    /*get restaurant from database by Id*/
    @Override
    public Restaurant get(int id) {
        return em.find(Restaurant.class,id);
    }

    /*get restaurant from database by Id with collection of
    *menuLists were issued by the restaurant*/
    @Override
    public Restaurant getWithMenuLists(int id) {
        Restaurant result = (Restaurant)em.createNamedQuery(Restaurant.GET_WITH_MENU_LISTS)
                            .setParameter("id",id)
                            .getSingleResult();
        List<Order> orders = ((Restaurant)em.createNamedQuery(Restaurant.GET_WITH_ORDERS)
                            .setParameter("id",id)
                            .getSingleResult()).getOrders();
        result.setOrders(orders);
        return result;

    }
}
