package com.votingsystem.service;

import com.votingsystem.entity.Vote;
import com.votingsystem.exceptions.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteService {

    List<Vote> getAll();

    Vote getById(int id) throws EntityNotFoundException;

    Vote save(Vote vote);

    void delete(int id);

    List<Vote> getByRestaurantId(int restaurant_id);

    List<Vote> getByUserId(int user_id);

    Vote getByUserIdAndDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) throws EntityNotFoundException;

    int getCountByRestaurantId(LocalDateTime beginCurrentDay, LocalDateTime endCurrentDay, int restaurant_id);
}
