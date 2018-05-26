package com.votingsystem.to.converters;

import com.votingsystem.entity.User;
import com.votingsystem.to.UserTo;

/**
 * ************************************
 * TO converter for User entity
 * ************************************
 */

public class UserConverter {

    //Get UserTo from User
    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }

}
