package com.votingsystem;

import com.votingsystem.entity.Role;
import com.votingsystem.entity.User;

/**
 * @author Paz1506
 * Тестовые данные, для
 * работы с пользователями.
 */

public class UserTestData {

    public static final int ID_USER = 19;
    public static final int ID_ADMIN = 20;

    public static final User USER = new User(ID_USER, "User", "user@yandex.ru", "1234", Role.ROLE_USER);
    public static final User ADMIN = new User(ID_ADMIN, "User", "admin@gmail.com", "1234", Role.ROLE_ADMIN);

}
