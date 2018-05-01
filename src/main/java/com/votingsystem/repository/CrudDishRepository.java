package com.votingsystem.repository;

import com.votingsystem.entity.Dish;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudDishRepository extends JpaRepository<Dish, Integer> {

    @Override
    List<Dish> findAll(Sort sort);

    @Override
    Dish save(Dish dish);

    @Override
    Optional<Dish> findById(Integer integer);

    @Override
    @Modifying
    void delete(Dish dish);
}
