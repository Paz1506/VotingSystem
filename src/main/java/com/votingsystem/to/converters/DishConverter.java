package com.votingsystem.to.converters;

import com.votingsystem.entity.Dish;
import com.votingsystem.to.DishTo;

/**
 * @author Paz1506
 * Converter DishTo - Dish.
 */

public class DishConverter {

    //Get DishTo from Dish
    public static DishTo getToFromDish(Dish dish) {
        return new DishTo(dish.getId(), dish.getName(), dish.getPrice());
    }

    //GetDish from TO (without Menu (-> set to сontroller))
    public static Dish getDishFromTo(DishTo newDish) {
        Dish dish = new Dish();
        dish.setId(newDish.getId());
        dish.setName(newDish.getName());
        dish.setPrice(newDish.getPrice());
        return dish;
    }
}
