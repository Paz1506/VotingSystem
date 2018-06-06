package com.votingsystem.repository;

import com.votingsystem.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Query("SELECT r FROM Restaurant r WHERE r.id IN (SELECT m.restaurant.id FROM Menu m WHERE m.date BETWEEN :startDateTime AND :endDateTime)")
    List<Restaurant> findAllRestaurantByCurrentDayWithMenu(@Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);

//    List<Restaurant> findAll();
//
//    @Transactional
//    Restaurant save(Restaurant restaurant);
//
//    Optional<Restaurant> findById(Integer integer);
//
//    @Modifying
//    @Transactional
//    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
//    void delete(@Param("id") int id);
}
