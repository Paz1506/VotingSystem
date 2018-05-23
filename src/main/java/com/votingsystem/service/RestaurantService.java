package com.votingsystem.service;

import com.votingsystem.entity.Restaurant;

import java.util.List;

public interface RestaurantService {

    List<Restaurant> getAll();

    Restaurant getById(int id);

    Restaurant save(Restaurant restaurant);

    void delete(int id);
}
