package com.votingsystem.service;

import com.votingsystem.entity.Menu;
import com.votingsystem.exceptions.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface MenuService {

    List<Menu> getAll();

    Menu getById(int id) throws EntityNotFoundException;

    Menu getByIdAndRestaurantId(int id, int restaurant_id) throws EntityNotFoundException;

    Menu save(Menu menu);

    void delete(int id);

    List<Menu> getByRestaurantId(int id);

    List<Menu> getAllMenusOfRestaurantByCurrentDay(int restaurant_id, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
