package com.votingsystem.repository;

import com.votingsystem.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Integer> {

    List<Vote> findByRestaurantId(Integer restaurant_id);

    List<Vote> findByUserId(Integer user_id);

    @Query("SELECT v FROM Vote v JOIN FETCH v.user WHERE v.user.id=:user_id AND v.dateTime BETWEEN :startDateTime AND :endDateTime")
    Vote findByUserIdAndDateTime(@Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime, @Param("user_id") int userId);

    @Query("SELECT COUNT (v.id) FROM Vote v WHERE v.restaurant.id=:restaurant_id AND v.dateTime BETWEEN :startDateTime AND :endDateTime")
    int findCountByRestaurantId(@Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime, @Param("restaurant_id") int restaurant_id);

}
