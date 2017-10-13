package ru.agorbunov.restaurant.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * Represents roles of user
 */
public enum Role implements GrantedAuthority {
    ROLE_USER,
    ROLE_ADMIN ;

    @Override
    public String getAuthority() {
            return name();
        }
}
