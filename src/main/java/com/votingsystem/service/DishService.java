package com.votingsystem.service;

import com.votingsystem.entity.Dish;
import com.votingsystem.exceptions.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface DishService {

    List<Dish> getAll();

    Dish getById(int id) throws EntityNotFoundException;

    Dish save(Dish menu);

    void delete(int id);

    List<Dish> getByMenuId(int id);

    List<Dish> getOfMenuByCurrentDay(int id, LocalDateTime startDateTime, LocalDateTime endDateTime);

    Dish getByRestAndMenuAndId(int dish_id, int menu_id, int restaurant_id) throws EntityNotFoundException;

}
