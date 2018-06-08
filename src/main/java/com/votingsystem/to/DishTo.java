package com.votingsystem.to;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Paz1506
 * Transfer object for Dish entity.
 */

public class DishTo extends RootTo implements Serializable {

    public DishTo() {
    }

    public DishTo(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public DishTo(Integer id, String name, Integer price) {
        super(id);
        this.name = name;
        this.price = price;
    }

    @NotBlank
    @NotNull
    @Size(min = 3, max = 100, message = "length must between 3 and 100 characters")
    private String name;

    @NotNull
    @Range(min = 50, max = 10000, message = "value must between 50 and 10000")
    private Integer price;


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


    @Override
    public String toString() {
        return "DishTo{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", id=" + id +
                '}';
    }
}
