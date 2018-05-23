package com.votingsystem.service;

import com.votingsystem.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(int id);

    User getByEmail(String email);

    User save(User user);

    void delete(int id);
}
