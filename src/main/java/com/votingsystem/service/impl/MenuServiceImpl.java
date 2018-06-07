package com.votingsystem.service.impl;

import com.votingsystem.entity.Menu;
import com.votingsystem.exceptions.EntityNotFoundException;
import com.votingsystem.repository.MenuRepository;
import com.votingsystem.service.MenuService;
import com.votingsystem.util.DataValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Autowired
    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public List<Menu> getAll() {
        return menuRepository.findAll();
    }

    @Override
    public Menu getById(int id) throws EntityNotFoundException {
        return DataValidationUtil.validNotFound(menuRepository.findById(id).orElse(null), id);
    }

    @Override
    public Menu getByIdAndRestaurantId(int id, int restaurant_id) throws EntityNotFoundException {
        return DataValidationUtil.validNotFound(menuRepository.findByIdAndRestaurantId(id, restaurant_id), id);
    }

    @Override
    @Transactional
    public Menu save(Menu menu) {
        return menuRepository.saveAndFlush(menu);
    }

    @Override
    @Transactional
    public void delete(int id) {
        menuRepository.deleteById(id);
    }

    @Override
    public List<Menu> getByRestaurantId(int id) {
        return menuRepository.findByRestaurantId(id);
    }

    @Override
    public List<Menu> getAllMenusOfRestaurantByCurrentDay(int restaurant_id, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return menuRepository.findAllMenusOfRestaurantByCurrentDay(restaurant_id, startDateTime, endDateTime);
    }
}
