package com.votingsystem.service;

import com.votingsystem.entity.Menu;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface MenuService {

    List<Menu> getAll();

    Menu getById(int id);

    Menu save(Menu menu);

    void delete(int id);

    List<Menu> getByRestaurantId(int id);
}
