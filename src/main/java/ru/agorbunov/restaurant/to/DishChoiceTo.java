package ru.agorbunov.restaurant.to;

import ru.agorbunov.restaurant.model.Dish;

import java.util.ArrayList;
import java.util.List;

/**
 * Class applying in orders_dishes.jsp in menuList modal window
 * for ability add field of checkbox "choose"
 */
public class DishChoiceTo extends Dish {

    public static List<DishChoiceTo> createDishChoiceToList(List<Dish> dishes, int[] chooseIds){
        List<DishChoiceTo> result = new ArrayList<>();
        for (Dish dish : dishes){
            boolean contains = false;
            for (int id : chooseIds){
                if (id == dish.getId()) contains = true;
            }
            result.add(new DishChoiceTo(dish, contains));
        }
        return result;
    }

    private boolean choose;

    private DishChoiceTo(Dish dish, boolean choose){
        super(dish.getDescription(),dish.getPrice());
        setId(dish.getId());
        this.choose = choose;
    }
}
