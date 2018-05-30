package com.votingsystem.to.converters;

import com.votingsystem.entity.Dish;
import com.votingsystem.to.DishTo;

/**
 * ************************************
 * TO converter for Dish entity
 * ************************************
 */

public class DishConverter {

    //Get DishTo from Dish
    public static DishTo getToFromDish(Dish dish) {
        return new DishTo(dish.getId(), dish.getName(), dish.getPrice());
    }

    //GetDish from TO (without Menu (-> set to AppController))
    public static Dish getDishFromTo(DishTo newDish) {
        Dish dish = new Dish();
        dish.setId(newDish.getId());
        dish.setName(newDish.getName());
        dish.setPrice(newDish.getPrice());
        return dish;
    }
}
