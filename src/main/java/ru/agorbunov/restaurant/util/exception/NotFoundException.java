package ru.agorbunov.restaurant.util.exception;

/**
 * This handlers must be thrown in case not found entity
 * in repository layer
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
