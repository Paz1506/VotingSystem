package com.votingsystem.service;

import com.votingsystem.entity.Restaurant;
import com.votingsystem.exceptions.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface RestaurantService {

    List<Restaurant> getAll();

    Restaurant getById(int id) throws EntityNotFoundException;

    Restaurant save(Restaurant restaurant);

    void delete(int id);

    List<Restaurant> getAllRestaurantByCurrentDayWithMenu(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
