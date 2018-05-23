package com.votingsystem.repository;

import com.votingsystem.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Integer> {

    List<Vote> findByRestaurantId(Integer restaurant_id);

    List<Vote> findByUserId(Integer user_id);

}
