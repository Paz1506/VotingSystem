package com.votingsystem.service.impl;

import com.votingsystem.entity.Dish;
import com.votingsystem.repository.DishRepository;
import com.votingsystem.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;

    @Autowired
    public DishServiceImpl(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @Override
    public List<Dish> getAll() {
        return dishRepository.findAll();
    }

    @Override
    public Dish getById(int id) {
        return dishRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Dish save(Dish menu) {
        return dishRepository.saveAndFlush(menu);
    }

    @Override
    @Transactional
    public void delete(int id) {
        dishRepository.deleteById(id);
    }

    @Override
    public List<Dish> getByMenuId(int id) {
        return dishRepository.findByMenuId(id);
    }

    @Override
    public List<Dish> getOfMenuByCurrentDay(int id, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return dishRepository.findOfMenuByCurrentDay(id, startDateTime, endDateTime);
    }

    @Override
    public Dish getByRestAndMenuAndId(int dish_id, int menu_id, int restaurant_id) {
        return dishRepository.findByRestAndMenuAndId(dish_id, menu_id, restaurant_id);
    }
}
