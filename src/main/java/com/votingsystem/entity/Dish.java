package com.votingsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * ************************************
 * Entity "Dish"
 * Hierarchy:
 * * Restaurant
 * * \_ Menu
 * * * \_Dish
 * ************************************
 */

@Entity
@Table(name = "dish")
public class Dish extends AbstractEntity {

    @Column(name = "name", nullable = false)
    @NotBlank
    @NotNull
    @Size(min = 3, max = 100)
    private String name;

    @Column(name = "price", nullable = false)
    @NotNull
    @Range(min = 50, max = 10000)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
//    @NotNull - TODO -сделать TO, иначе ошибка валидации
//    @JsonIgnore // в ТО сделать только айдишник меню
    private Menu menu;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "name='" + name + '\'' +
                ", price=" + price +
//                ", menu_id=" + menu.getId() + //Это сделать в ТО
                ", id=" + id +
                '}';
    }
}
