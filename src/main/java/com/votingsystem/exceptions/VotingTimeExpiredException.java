package com.votingsystem.exceptions;

/**
 * @author Paz1506
 * Custom exception, throws if
 * user voted after 11:00 of
 * current day.
 */

public class VotingTimeExpiredException extends Exception {

    @Override
    public String getMessage() {
        return "The voting is allowed only until 11:00";
    }
}
