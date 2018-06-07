package com.votingsystem.util;

import com.votingsystem.exceptions.EntityNotFoundException;

/**
 * @author Paz1506
 * Checking data for errors
 * and nullable values.
 */

public class DataValidationUtil {

    /**
     * The method checks that the transmitted data is not Null,
     * otherwise it throws custom EntityNotFoundException.
     *
     * @param entity
     * @param id
     * @param <T>
     * @return
     * @throws EntityNotFoundException
     */
    public static <T> T validNotFound(T entity, int id) throws EntityNotFoundException {
        if (entity != null) {
            return entity;
        } else {
            throw new EntityNotFoundException("Entity not found, id = " + id);
        }
    }
}
