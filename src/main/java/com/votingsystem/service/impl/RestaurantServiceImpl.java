package com.votingsystem.service.impl;

import com.votingsystem.entity.Restaurant;
import com.votingsystem.exceptions.EntityNotFoundException;
import com.votingsystem.repository.RestaurantRepository;
import com.votingsystem.service.RestaurantService;
import com.votingsystem.util.DataValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    @Cacheable("restaurants")
    public List<Restaurant> getAll() {
        return this.restaurantRepository.findAll();
    }

    @Override
    public Restaurant getById(int id) throws EntityNotFoundException {
        return DataValidationUtil.validNotFound(this.restaurantRepository.findById(id).orElse(null), id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant save(Restaurant restaurant) {
        return this.restaurantRepository.saveAndFlush(restaurant);
    }

    @Override
    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(int id) {
        this.restaurantRepository.deleteById(id);
    }

    @Override
    public List<Restaurant> getAllRestaurantByCurrentDayWithMenu(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return this.restaurantRepository.findAllRestaurantByCurrentDayWithMenu(startDateTime, endDateTime);
    }
}
