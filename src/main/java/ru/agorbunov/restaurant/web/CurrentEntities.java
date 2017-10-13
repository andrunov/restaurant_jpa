package ru.agorbunov.restaurant.web;

import ru.agorbunov.restaurant.model.*;

/**
 * Current entities of session
 */
class CurrentEntities {

    private static User currentUser;

    private static Restaurant currentRestaurant;

    private static MenuList currentMenuList;

    private static Order currentOrder;

    private static Dish currentDish;

    public CurrentEntities() {
    }

    static Restaurant getCurrentRestaurant() {
        return currentRestaurant;
    }

    static User getCurrentUser() {
        return currentUser;
    }

    static void setCurrentUser(User currentUser) {
        CurrentEntities.currentUser = currentUser;
    }

    static void setCurrentRestaurant(Restaurant currentRestaurant) {
        CurrentEntities.currentRestaurant = currentRestaurant;
    }

    static MenuList getCurrentMenuList() {
        return currentMenuList;
    }

    static void setCurrentMenuList(MenuList currentMenuList) {
        CurrentEntities.currentMenuList = currentMenuList;
    }

    static Order getCurrentOrder() {
        return currentOrder;
    }

    static void setCurrentOrder(Order currentOrder) {
        CurrentEntities.currentOrder = currentOrder;
    }

    static Dish getCurrentDish() {
        return currentDish;
    }

    static void setCurrentDish(Dish currentDish) {
        CurrentEntities.currentDish = currentDish;
    }
}
