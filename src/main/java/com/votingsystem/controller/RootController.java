package com.votingsystem.controller;

import com.votingsystem.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class RootController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    final static String ROOT_VERSION_URL = "/v1.0";
    final static String RESTAURANTS_URL = "/restaurants";
    final static String MENUS_URL = "/menus";
    final static String DISHES_URL = "/dishes";
    final static String VOTES_URL = "/votes";
    final static String USERS_URL = "/users";
    final static String ADMIN = "/admin";


    protected final RestaurantService restaurantService;
    protected final MenuService menuService;
    protected final DishService dishService;
    protected final VoteService voteService;
    protected final UserService userService;

    @Autowired
    public RootController(RestaurantService restaurantService, MenuService menuService, DishService dishService, VoteService voteService, UserService userService) {
        this.restaurantService = restaurantService;
        this.menuService = menuService;
        this.dishService = dishService;
        this.voteService = voteService;
        this.userService = userService;
    }
}
