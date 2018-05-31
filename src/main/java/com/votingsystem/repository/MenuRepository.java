package com.votingsystem.repository;

import com.votingsystem.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {

    List<Menu> findByRestaurantId(@Param("restaurant_id") Integer id);

    @Query("SELECT m FROM Menu m where m.restaurant.id=:restaurant_id AND m.date BETWEEN :startDateTime AND :endDateTime")
    List<Menu> findAllMenusOfRestaurantByCurrentDay(@Param("restaurant_id") int restaurant_id, @Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);
}
