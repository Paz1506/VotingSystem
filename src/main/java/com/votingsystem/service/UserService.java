package com.votingsystem.service;

import com.votingsystem.entity.User;
import com.votingsystem.exceptions.EntityNotFoundException;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(int id) throws EntityNotFoundException;

    User getByEmail(String email);

    User save(User user);

    void delete(int id);
}
