package com.votingsystem.exceptions;

/**
 * @author Paz1506
 * Custom exception, throws if
 * entity not found.
 */

public class EntityNotFoundException extends Exception {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
