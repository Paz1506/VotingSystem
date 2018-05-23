package com.votingsystem.repository;

import com.votingsystem.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {

    List<Menu> findByRestaurantId(@Param("restaurant_id") Integer id);
}
