package com.votingsystem.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * ************************************
 * Сущность "Ресторан"
 * Поля:
 * - имя
 * - список меню (сущность "Меню") -- Убрать?
 * ************************************
 */

//@NamedQueries({
//        @NamedQuery(name = Restaurant.ALL, query = "SELECT r FROM Restaurant r")
//})

@Entity
@Table(name = "restaurant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Restaurant extends AbstractEntity {

//    public static final String ALL = "Restaurant.getAll";

    @Column(name = "name", nullable = false)
    @NotNull
    @Size(min = 3, max = 100)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*@OneToMany(mappedBy = "menu")
    private List<Menu> menus;*/
}
