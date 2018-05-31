package com.votingsystem.service.impl;

import com.votingsystem.entity.Menu;
import com.votingsystem.repository.MenuRepository;
import com.votingsystem.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
    public Menu getById(int id) {
        return menuRepository.findById(id).orElse(null);
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
