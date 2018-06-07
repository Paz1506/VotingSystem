package com.votingsystem.repository;

import com.votingsystem.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Integer> {

    List<Dish> findByMenuId(@Param("menu_id") Integer id);

    @Query("SELECT d FROM Dish d where d.menu.id=:menu_id AND d.menu.date BETWEEN :startDateTime AND :endDateTime")
    List<Dish> findOfMenuByCurrentDay(@Param("menu_id") int menu_id, @Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);

    @Query("SELECT d FROM Dish d where d.id=:dish_id AND d.menu.id=:menu_id AND d.menu.restaurant.id=:restaurant_id")
    Dish findByRestAndMenuAndId(@Param("dish_id") int dish_id, @Param("menu_id") int menu_id, @Param("restaurant_id") int restaurant_id);
}
