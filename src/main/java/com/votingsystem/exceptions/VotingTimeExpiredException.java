package com.votingsystem.exceptions;

public class VotingTimeExpiredException extends Exception {

    @Override
    public String getMessage() {
        return "The voting is allowed only until 11:00";
    }
}
