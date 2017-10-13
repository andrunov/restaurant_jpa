package ru.agorbunov.restaurant.util;

import ru.agorbunov.restaurant.model.Role;

/**
 * Class for different parsing methods
 */
public class ParsingUtil {
    /*parsing String array of Status enum values*/
    public static Role[] parseRoles(String[] roleValues){
        Role[] result = new Role[roleValues.length];
        for (int i = 0; i < roleValues.length; i++){
            result[i] = Role.valueOf(roleValues[i]);
        }
        return result;
    }
}
