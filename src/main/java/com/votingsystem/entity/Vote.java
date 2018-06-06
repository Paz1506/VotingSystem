package com.votingsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author Paz1506
 * Vote entity.
 */

@Entity
@Table(name = "vote")
public class Vote extends AbstractEntity {

    public Vote() {
    }

    public Vote(User user, LocalDateTime dateTime, Restaurant restaurant) {
        this.user = user;
        this.dateTime = dateTime;
        this.restaurant = restaurant;
    }

    public Vote(Integer id, User user, LocalDateTime dateTime, Restaurant restaurant) {
        super(id);
        this.user = user;
        this.dateTime = dateTime;
        this.restaurant = restaurant;
    }

    //    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    @JsonIgnore //TODO убрать и сделать через TO
    private User user;

    @Column(name = "date_time", nullable = false)
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dateTime;

    //    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonIgnore // else stackoverflow http://localhost:8080/v1.0/restaurants/1/menus
    private Restaurant restaurant;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "user_id=" + user.getId() +
                ", dateTime=" + dateTime +
                ", restaurant_id=" + restaurant.getId() +
                ", id=" + id +
                '}';
    }
}
