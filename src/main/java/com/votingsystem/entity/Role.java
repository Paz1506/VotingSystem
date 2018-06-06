package com.votingsystem.entity;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author Paz1506
 * List of user roles
 */

public enum Role implements GrantedAuthority {
    ROLE_USER,
    ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}