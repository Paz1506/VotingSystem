package com.votingsystem.repository;

import com.votingsystem.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

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
