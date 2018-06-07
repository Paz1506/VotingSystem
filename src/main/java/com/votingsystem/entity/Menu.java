package com.votingsystem.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Paz1506
 * Menu entity.
 */

@Entity
@Table(name = "menu")
public class Menu extends AbstractEntity {

    public Menu() {
    }

    public Menu(String name, LocalDateTime date, List<Dish> dishes, Restaurant restaurant) {
        this.name = name;
        this.date = date;
        this.dishes = dishes;
        this.restaurant = restaurant;
    }

    public Menu(Integer id, String name, LocalDateTime date, List<Dish> dishes, Restaurant restaurant) {
        super(id);
        this.name = name;
        this.date = date;
        this.dishes = dishes;
        this.restaurant = restaurant;
    }

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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu", cascade = CascadeType.ALL)
    @OrderBy("id DESC")
    private List<Dish> dishes;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "restaurant_id", nullable = false)
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
