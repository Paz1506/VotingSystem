//package com.votingsystem.repository.impl;
//
//import com.votingsystem.entity.Restaurant;
//import com.votingsystem.repository.RestaurantRepository;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//@Transactional(readOnly = true)
//public class RestaurantRepositoryImpl implements RestaurantRepository {
//
//    @PersistenceContext
//    private EntityManager em;
//
//    @Override
//    public List<Restaurant> findAll() {
//        return em.createNamedQuery(Restaurant.ALL, Restaurant.class).getResultList();
//    }
//
//    @Override
//    public Restaurant save(Restaurant restaurant) {
//        if (restaurant.isNew()) {
//            em.persist(restaurant);
//            return restaurant;
//        } else {
//            return em.merge(restaurant);
//        }
//    }
//
//    @Override
//    public Optional<Restaurant> findById(Integer integer) {
//        return Optional.empty();
//    }
//
//    @Override
//    public void delete(Restaurant restaurant) {
//
//    }
//}
