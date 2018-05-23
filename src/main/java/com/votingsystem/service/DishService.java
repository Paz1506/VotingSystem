package com.votingsystem.service;

import com.votingsystem.entity.Dish;

import java.util.List;

public interface DishService {

    List<Dish> getAll();

    Dish getById(int id);

    Dish save(Dish menu);

    void delete(int id);

    List<Dish> getByMenuId(int id);

}
