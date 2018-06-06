package com.votingsystem.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Paz1506
 * Restaurant entity.
 */

//@NamedQueries({
//        @NamedQuery(name = Restaurant.ALL, query = "SELECT r FROM Restaurant r")
//})

@Entity
@Table(name = "restaurant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Restaurant extends AbstractEntity {

    public Restaurant() {
    }

    public Restaurant(String name) {
        this.name = name;
    }

    public Restaurant(Integer id, String name) {
        super(id);
        this.name = name;
    }

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
