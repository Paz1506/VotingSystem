package com.votingsystem.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ************************************
 * Сущность "Меню"
 * Поля:
 * - дата (date)
 * - список блюд dishes (сущность "Блюдо") -- Убрать?
 * - ресторан (сущность "Ресторан") -- Убрать?
 * ************************************
 */

@Entity
@Table(name = "menu")
public class Menu extends AbstractEntity {

    /**
     * Date activity menu
     */
    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDateTime date;

    @OneToMany(mappedBy = "menu")
    private List<Dish> dishes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;


    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "date=" + date +
                ", dishes=" + dishes +
                ", restaurant=" + restaurant +
                ", id=" + id +
                '}';
    }
}
