package com.votingsystem.repository;

import com.votingsystem.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Integer> {
    List<Dish> findByMenuId(@Param("menu_id") Integer id);
}
