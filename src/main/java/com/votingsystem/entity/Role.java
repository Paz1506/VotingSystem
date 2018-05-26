package com.votingsystem.entity;

import org.springframework.security.core.GrantedAuthority;

/**
 * ************************************
 * Список ролей пользователя
 * ************************************
 */

public enum Role implements GrantedAuthority {
    ROLE_USER,
    ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}