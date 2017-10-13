package ru.agorbunov.restaurant.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.agorbunov.restaurant.model.Dish;
import ru.agorbunov.restaurant.model.MenuList;
import ru.agorbunov.restaurant.model.Restaurant;
import ru.agorbunov.restaurant.repository.MenuListRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * MenuList-entities repository by Java Persistence API
 */
@Repository
@Transactional(readOnly = true)
public class JpaMenuListRepositoryImpl implements MenuListRepository {

    @PersistenceContext
    private EntityManager em;

    /*save menuList in database, restaurantId in parameters is Id
    *of restaurant to which the menuList is belong*/
    @Override
    @Transactional
    public MenuList save(MenuList menuList, int restaurantId) {
        if (!menuList.isNew() && get(menuList.getId(), restaurantId) == null) {
            return null;
        }
        menuList.setRestaurant(em.getReference(Restaurant.class, restaurantId));
        if (menuList.isNew()){
            em.persist(menuList);
            return menuList;
        }else {
            return em.merge(menuList);
        }
    }

    /*delete menuList from database by Id */
    @Override
    @Transactional
    public boolean delete(int id) {
        return em.createNamedQuery(MenuList.DELETE)
                .setParameter("id", id)
                .executeUpdate() !=0;
    }

    /*get all menuLists from database*/
    @Override
    public List<MenuList> getAll() {
        return em.createNamedQuery(MenuList.GET_ALL, MenuList.class).getResultList();
    }

    /*get menuList from database by Id, restaurantId in parameters is Id
    *of restaurant to which the menuList is belong*/
    @Override
    public MenuList get(int id, int restaurantId) {
        MenuList menuList = em.find(MenuList.class,id);
        return menuList != null && menuList.getRestaurant().getId() == restaurantId ? menuList : null;
    }

    /*get menuList from database by Id with collection of dishes which the menuList is have,
    * restaurantId in parameters is Id of restaurant to which the menuList is belong*/
    @Override
    public MenuList getWithDishes(int id, int restaurantId) {
        MenuList menuList = (MenuList)em.createNamedQuery(MenuList.GET_WITH_DISHES)
                                        .setParameter("id",id)
                                        .getSingleResult();
        return menuList != null && menuList.getRestaurant().getId() == restaurantId ? menuList : null;
    }

    /*get all menuLists from database that belongs to restaurant with Id pass as parameter */
    @Override
    public List<MenuList> getByRestaurant(int restaurantId) {
        return em.createNamedQuery(MenuList.GET_ALL_BY_RESTAURANT, MenuList.class)
                                    .setParameter("restaurantId",restaurantId)
                                    .getResultList();
    }

   /*get all menuLists from database that belongs to restaurant with Id pass as 1st parameter
    * and according to status pass as 2nd parameter*/
    @Override
    public List<MenuList> getByRestaurantAndEnabled(int restaurantId, boolean enabled) {
        return em.createNamedQuery(MenuList.GET_ALL_BY_RESTAURANT_AND_ENABLED, MenuList.class)
                .setParameter("restaurantId",restaurantId)
                .setParameter("enabled",enabled)
                .getResultList();
    }

    /*get menuList from database by dish Id, belongs to this menu list*/
    @Transactional
    @Override
    public MenuList getByDish(int dishId) {
        Dish dish = em.find(Dish.class, dishId);
        if (dish == null) return null;
        MenuList menuList = (MenuList)em.createNamedQuery(MenuList.GET_WITH_DISHES)
                .setParameter("id",dish.getMenuList().getId())
                .getSingleResult();
        return menuList != null && menuList.getDishList().contains(dish) ? menuList : null;
    }
}
