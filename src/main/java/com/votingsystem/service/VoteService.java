package com.votingsystem.service;

import com.votingsystem.entity.Restaurant;
import com.votingsystem.entity.Vote;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteService {

    List<Vote> getAll();

    Vote getById(int id);

    Vote save(Vote vote);

    void delete(int id);

    List<Vote> getByRestaurantId(int restaurant_id);

    List<Vote> getByUserId(int user_id);

    Vote getByUserIdAndDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId);
}
