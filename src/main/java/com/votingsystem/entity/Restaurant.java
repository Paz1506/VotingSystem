package com.votingsystem.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "restaurant")
public class Restaurant extends AbstractEntity {

    @Column(name = "name", nullable = false)
    @NotNull
    private String name;

    @OneToMany(mappedBy = "menu")
    private List<Menu> menus;
}
