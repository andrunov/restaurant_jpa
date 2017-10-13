package ru.agorbunov.restaurant.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.agorbunov.restaurant.model.Dish;
import ru.agorbunov.restaurant.model.MenuList;
import ru.agorbunov.restaurant.model.Order;
import ru.agorbunov.restaurant.repository.DishRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

/**
 * Dish-entities repository by Java Persistence API
 */
@Repository
@Transactional(readOnly = true)
public class JpaDishRepositoryImpl implements DishRepository {

    @PersistenceContext
    private EntityManager em;

    /*save dish in database, menulistId in parameters is Id of menu list to which the dish is belong*/
    @Override
    @Transactional
    public Dish save(Dish dish, int menuListId) {
        if (!dish.isNew() && get(dish.getId(), menuListId) == null) {
            return null;
        }
        dish.setMenuList(em.getReference(MenuList.class, menuListId));

        if (dish.isNew()){
            em.persist(dish);
            return dish;
        }else {
            return em.merge(dish);
        }
    }

    /*delete dish from database by Id */
    @Override
    @Transactional
    public boolean delete(int id) {
        return em.createNamedQuery(Dish.DELETE)
                .setParameter("id", id)
                .executeUpdate() !=0;
    }

    /*get all dishes from database*/
    @Override
    public List<Dish> getAll() {
        return em.createNamedQuery(Dish.GET_ALL, Dish.class).getResultList();
    }

    /*get dish in database, menulistId in parameters is Id of menu list to which the dish is belong*/
    @Override
    public Dish get(int id, int menuListId) {
        Dish dish = em.find(Dish.class, id);
        return dish != null && dish.getMenuList().getId() == menuListId ? dish : null;
    }

    /*get dish from database bi Id with collection of orders which contains the dish,
    * menulistId in parameters is Id of menu list to which the dish is belong*/
    @Override
    public Dish getWithOrders(int id, int menuListId) {
        Dish dish = (Dish)em.createNamedQuery(Dish.GET_WITH_ORDERS)
                                        .setParameter("id",id)
                                        .getSingleResult();
        return dish != null && dish.getMenuList().getId() == menuListId ? dish : null;
    }

    /*get all dishes from database that belongs to menuList with Id pass as parameter */
    @Override
    public List<Dish> getByMenuList(int menuListId) {
        return em.createNamedQuery(Dish.GET_ALL_BY_MENU_LIST, Dish.class)
                .setParameter("menuListId",menuListId)
                .getResultList();
    }

    /*get all dishes-entities from database that belongs to order with Id pass as parameter */
    @Override
    public Map<Dish,Integer> getByOrder(int orderId) {
        List<Order> result = em.createNamedQuery(Dish.GET_ALL_BY_ORDER, Order.class)
                .setParameter("orderId",orderId)
                .getResultList();
        return result.get(0).getDishes();
    }

    /*delete references to dishes from order in database, dish Id and order Id pass in parameters */
    @Override
    @Transactional
    public boolean deleteFromOrder(int id, int orderId) {
        return em.createNamedQuery(Dish.DELETE_FROM_ORDERS)
                .setParameter(1, id)
                .setParameter(2,orderId)
                .executeUpdate() !=0;
    }
}
