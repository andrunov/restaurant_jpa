package ru.agorbunov.restaurant.util.exception;

/**
 * This handlers throws somewhere if need to refuse updates
 */
public class RefuseToUpdateException extends RuntimeException {
    public RefuseToUpdateException(String message) {
        super(message);
    }
}
