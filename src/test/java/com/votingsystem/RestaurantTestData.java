package com.votingsystem;

import com.votingsystem.entity.Restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Paz1506
 * Тестовые данные, для
 * работы с ресторанами.
 */

public class RestaurantTestData {

    public static final int ID_RESTAURANT_1 = 1;
    public static final int ID_RESTAURANT_2 = 2;
    public static final int ID_RESTAURANT_3 = 3;
    public static final int ID_RESTAURANT_4 = 4;

    public static final Restaurant RESTAURANT_1 = new Restaurant(ID_RESTAURANT_1, "Restaurant_1");
    public static final Restaurant RESTAURANT_2 = new Restaurant(ID_RESTAURANT_2, "Restaurant_2");
    public static final Restaurant RESTAURANT_3 = new Restaurant(ID_RESTAURANT_3, "Restaurant_3");
    public static final Restaurant RESTAURANT_4 = new Restaurant(ID_RESTAURANT_4, "Restaurant_4");

    public static final List<Restaurant> RESTAURANT_LIST = new ArrayList<>();

    static {
        RESTAURANT_LIST.add(RESTAURANT_1);
        RESTAURANT_LIST.add(RESTAURANT_2);
        RESTAURANT_LIST.add(RESTAURANT_3);
        RESTAURANT_LIST.add(RESTAURANT_4);
    }
}
