package com.votingsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    @Column(name = "name", nullable = false)
    @NotNull
    @NotBlank
    @Size(min = 3, max = 100)
    private String name;

    /**
     * Date activity menu
     */
    @Column(name = "date", nullable = false)
    @NotNull
//        @NotNull - TODO -сделать TO, иначе ошибка валидации
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "menu"/*, cascade = CascadeType.ALL, orphanRemoval = true*/)
//    @NotNull //- TODO -сделать TO, иначе ошибка валидации
    private List<Dish> dishes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
//    @JsonIgnore // else stackoverflow http://localhost:8080/v1.0/restaurants/1/menus
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", restaurant_id=" + restaurant.getId() +
                ", id=" + id +
                '}';
    }
}
