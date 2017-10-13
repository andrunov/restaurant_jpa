package ru.agorbunov.restaurant.util.exception;

/**
 * This exceptions must be thrown in case when two arrays have different size
 * though these arrays must have equals size
 */
public class ArraysIncompatibilityException extends RuntimeException {
    public ArraysIncompatibilityException(String message) {
        super(message);
    }
}
