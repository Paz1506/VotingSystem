package com.votingsystem.controller;

import com.votingsystem.entity.User;
import com.votingsystem.entity.Vote;
import com.votingsystem.exceptions.VotingTimeExpiredException;
import com.votingsystem.security.AuthUser;
import com.votingsystem.service.*;
import com.votingsystem.to.DishTo;
import com.votingsystem.to.UserTo;
import com.votingsystem.to.converters.DishConverter;
import com.votingsystem.to.converters.UserConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Основной контроллер приложения
 * Контроллер для работы с:
 * - Ресторанами
 * - Меню
 * - Блюдами
 * Создан: ZPS 2018.05.19
 */

public class AppController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    final static String ROOT_VERSION_URL = "/v1.0";
    final static String RESTAURANTS_URL = "/restaurants";
    final static String MENUS_URL = "/menus";
    final static String DISHES_URL = "/dishes";
    final static String VOTES_URL = "/votes";
    final static String USERS_URL = "/users";
    final static String ADMIN = "/admin";

    protected final static LocalTime tresholdTime = LocalTime.of(11, 0, 0);

    protected final RestaurantService restaurantService;
    protected final MenuService menuService;
    protected final DishService dishService;
    protected final VoteService voteService;
    protected final UserService userService;

    @Autowired
    public AppController(RestaurantService restaurantService, MenuService menuService, DishService dishService, VoteService voteService, UserService userService) {
        this.restaurantService = restaurantService;
        this.menuService = menuService;
        this.dishService = dishService;
        this.voteService = voteService;
        this.userService = userService;
    }


    //=========================================
    //Работа с ресторанами
    //=========================================












    //=========================================
    //Работа с блюдами
    //=========================================



}
