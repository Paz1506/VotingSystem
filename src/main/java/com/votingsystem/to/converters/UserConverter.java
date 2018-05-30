package com.votingsystem.to.converters;

import com.votingsystem.entity.Role;
import com.votingsystem.entity.User;
import com.votingsystem.to.UserTo;

import java.util.HashSet;
import java.util.Set;

/**
 * ************************************
 * TO converter for User entity
 * ************************************
 */

public class UserConverter {

    //Get UserTo from User
    public static UserTo getToFromUser(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }

    //GetUser from TO
    public static User getUserFromTo(UserTo newUser) {
        User user = new User();
        user.setId(newUser.getId());
        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        user.setEnabled(true);
        user.setPassword(newUser.getPassword());
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        user.setRoles(roles);
        return user;
    }

}
