package com.votingsystem.service.impl;

import com.votingsystem.entity.Vote;
import com.votingsystem.repository.VoteRepository;
import com.votingsystem.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;

    @Autowired
    public VoteServiceImpl(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Override
    public List<Vote> getAll() {
        return voteRepository.findAll();
    }

    @Override
    public Vote getById(int id) {
        return voteRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Vote save(Vote vote) {
        return voteRepository.saveAndFlush(vote);
    }

    @Override
    @Transactional
    public void delete(int id) {
        voteRepository.deleteById(id);
    }

    @Override
    public List<Vote> getByRestaurantId(int restaurant_id) {
        return voteRepository.findByRestaurantId(restaurant_id);
    }

    @Override
    public List<Vote> getByUserId(int user_id) {
        return voteRepository.findByUserId(user_id);
    }

    @Override
    public Vote getByUserIdAndDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return voteRepository.findByUserIdAndDateTime(startDateTime, endDateTime, userId);
    }
}
