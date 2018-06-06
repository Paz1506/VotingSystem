package com.votingsystem.controller;

import com.votingsystem.entity.*;
import com.votingsystem.security.AuthUser;
import com.votingsystem.service.*;
import com.votingsystem.to.DishTo;
import com.votingsystem.to.MenuTo;
import com.votingsystem.to.UserTo;
import com.votingsystem.to.converters.DishConverter;
import com.votingsystem.to.converters.MenuConverter;
import com.votingsystem.to.converters.UserConverter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.votingsystem.controller.RootController.ADMIN;
import static com.votingsystem.controller.RootController.ROOT_VERSION_URL;
import static com.votingsystem.util.DateTimeUtil.safeBeginCurrentDay;
import static com.votingsystem.util.DateTimeUtil.safeEndCurrentDay;

/**
 * @author Paz1506
 * Контроллер, обрабатывающий запросы
 * администраторов системы.
 */

@RestController
@RequestMapping(value = ROOT_VERSION_URL + ADMIN)
public class AdminController extends RootController {

    public AdminController(RestaurantService restaurantService, MenuService menuService, DishService dishService, VoteService voteService, UserService userService) {
        super(restaurantService, menuService, dishService, voteService, userService);
    }

    //restaurants

    /**
     * Возвращает все рестораны.
     *
     * @return
     */
    @GetMapping(value = RESTAURANTS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Restaurant> getAllRestaurants() {
        log.info("User {} get list restaurants", AuthUser.id());
        return restaurantService.getAll();
    }

    /**
     * Создание/обновление ресторана.
     *
     * @param restaurant
     */
    @PostMapping(value = RESTAURANTS_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createOrUpdateRestaurant(@Valid Restaurant restaurant) {
        log.info("User {} create/update restaurant {}", AuthUser.id(), restaurant);
        restaurantService.save(restaurant);
    }

    /**
     * Возвращает ресторан по id.
     *
     * @param restaurant_id
     * @return
     */
    @GetMapping(value = RESTAURANTS_URL + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Restaurant getRestaurantById(@PathVariable("id") int restaurant_id) {
        log.info("User {} get restaurant {}", AuthUser.id(), restaurant_id);
        return restaurantService.getById(restaurant_id);
    }

    /**
     * Удаляет ресторан по id.
     *
     * @param restaurant_id
     */
    @DeleteMapping(value = RESTAURANTS_URL + "/{id}")
    public void deleteRestaurantById(@PathVariable("id") int restaurant_id) {
        log.info("User {} delete restaurant {}", AuthUser.id(), restaurant_id);
        restaurantService.delete(restaurant_id);
    }

    //menus

    /**
     * Возвращает все меню ресторана. //TODO Сделать фильтрацию по параметрам
     *
     * @param restaurant_id
     * @return
     */
    @GetMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MenuTo> getMenusByRestaurantId(@PathVariable("restaurant_id") int restaurant_id) {
        log.info("User {} get list menus for restaurant {}", AuthUser.id(), restaurant_id);
        return menuService.getByRestaurantId(restaurant_id)
                .stream()
                .map(MenuConverter::getToFromMenu)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает меню по id.
     *
     * @param menu_id
     * @return
     */
    @GetMapping(value = {MENUS_URL + "/{menu_id}", RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL + "/{menu_id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public MenuTo getMenuById(@PathVariable("menu_id") int menu_id) {
        log.info("User {} get menu {}", AuthUser.id(), menu_id);
        return MenuConverter.getToFromMenu(menuService.getById(menu_id));
    }

    /**
     * Создание/обновление меню.
     *
     * @param restaurant_id
     * @param menuTo
     */
    @PostMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createOrUpdateMenu(@PathVariable("restaurant_id") int restaurant_id, @Valid MenuTo menuTo) {
        log.info("User {} create/update menu {} for restaurant {}", AuthUser.id(), menuTo, restaurant_id);
        Menu menu = MenuConverter.getMenuFromTo(menuTo);
        menu.setRestaurant(restaurantService.getById(restaurant_id));
        menuService.save(menu);
    }

    /**
     * Удаляет меню по id.
     *
     * @param menu_id
     */
    @DeleteMapping(value = {MENUS_URL + "/{menu_id}", RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL + "/{menu_id}"})
    public void deleteMenuById(@PathVariable("menu_id") int menu_id) {
        log.info("User {} delete menu {}", AuthUser.id(), menu_id);
        menuService.delete(menu_id);
    }

    //dishes

    /**
     * Возвращает все блюда в меню.
     *
     * @param menu_id
     * @return
     */
    @GetMapping(value = {RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL + "/{menu_id}" + DISHES_URL, MENUS_URL + "/{menu_id}" + DISHES_URL}, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DishTo> getDishesByMenuId(@PathVariable("menu_id") int menu_id) {
        return dishService.getByMenuId(menu_id)
                .stream()
                .map(DishConverter::getToFromDish)
                .collect(Collectors.toList());
    }

    /**
     * Создание/обновление блюда.
     *
     * @param menu_id
     * @param dishTo
     */
    @PostMapping(value = MENUS_URL + "/{menu_id}" + DISHES_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createOrUpdateDish(@PathVariable("menu_id") int menu_id, @Valid DishTo dishTo) {
        log.info("User {} create/update dish {} of menu {}", AuthUser.id(), dishTo, menu_id);
        Dish dish = DishConverter.getDishFromTo(dishTo);
        dish.setMenu(menuService.getById(menu_id));
        dishService.save(dish);
    }

    /**
     * Возвращает блюдо по id.
     *
     * @param dish_id
     * @return
     */
    @GetMapping(value = {RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL + "/{menu_id}" + DISHES_URL + "/{dish_id}", DISHES_URL + "/{dish_id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public DishTo getDishById(@PathVariable("dish_id") int dish_id) {
        log.info("User {} get dish {}", AuthUser.id(), dish_id);
        return DishConverter.getToFromDish(dishService.getById(dish_id));
    }

    /**
     * Удаляет блюдо по id.
     *
     * @param dish_id
     */
    @DeleteMapping(value = {MENUS_URL + "/{menu_id}" + DISHES_URL + "/{dish_id}", DISHES_URL + "/{dish_id}"})
    public void deleteDishById(@PathVariable("dish_id") int dish_id) {
        log.info("User {} delete dish {}", AuthUser.id(), dish_id);
        dishService.delete(dish_id);
    }

    //users

    /**
     * Возвращает всех пользователей.
     *
     * @return
     */
    @GetMapping(value = USERS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAllUsers() {
        log.info("User {} get list users", AuthUser.id());
        return userService.getAll();
    }

    /**
     * Создание/обновление пользователя.
     *
     * @param userTo
     */
    @PostMapping(value = USERS_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createOrUpdateUser(@Valid UserTo userTo) {
        log.info("User {} create/update user {}", AuthUser.id(), userTo);
        userService.save(UserConverter.getUserFromTo(userTo));
    }

    /**
     * Возвращает пользователя по id.
     *
     * @param user_id
     * @return
     */
    @GetMapping(value = USERS_URL + "/{user_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserById(@PathVariable("user_id") int user_id) {
        log.info("User {} get user {}", AuthUser.id(), user_id);
        return userService.getById(user_id);
    }

    /**
     * Удаляет пользователя по id.
     *
     * @param user_id
     */
    @DeleteMapping(value = USERS_URL + "/{user_id}")
    public void deleteUserById(@PathVariable("user_id") int user_id) {
        log.info("User {} delete user {}", AuthUser.id(), user_id);
        userService.delete(user_id);
    }

    //votes

    /**
     * Возвращает голоса пользователя за все время.
     *
     * @param user_id
     * @return
     */
    @GetMapping(value = USERS_URL + "/{user_id}" + VOTES_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Vote> getVotesByUser(@PathVariable("user_id") int user_id) {
        log.info("User {} get list votes with user {}", AuthUser.id(), user_id);
        return voteService.getByUserId(user_id);
    }

    /**
     * Возвращает голос по id.
     *
     * @param vote_id
     * @return
     */
    @GetMapping(value = VOTES_URL + "/{vote_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Vote getUserVoteById(@PathVariable("vote_id") int vote_id) {
        log.info("User {} get vote {}", AuthUser.id(), vote_id);
        return voteService.getById(vote_id);
    }

    /**
     * Возвращает все голоса ресторана за все время.
     *
     * @param restaurant_id
     * @return
     */
    @GetMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + VOTES_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Vote> getRestaurantVotesById(@PathVariable("restaurant_id") int restaurant_id) {
        log.info("User {} get restaurant {} votes", AuthUser.id(), restaurant_id);
        return voteService.getByRestaurantId(restaurant_id);
    }

    /**
     * Возвращает количество голосов ресторана.
     * По умолчанию возвращает данные за текущий
     * день (если не заданы параметры фильтрации).
     *
     * @param restaurant_id
     * @param startDate
     * @param endDate
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + VOTES_URL + "/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public int getAllCountRestaurantVotesById(
            @PathVariable("restaurant_id") int restaurant_id,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            @RequestParam(value = "startTime", required = false) LocalTime startTime,
            @RequestParam(value = "endTime", required = false) LocalTime endTime
    ) {
        log.info("User {} get restaurant {} count votes between {} {} and {} {}", AuthUser.id(), restaurant_id, startDate, startTime, endDate, endTime);
        return voteService.getCountByRestaurantId(safeBeginCurrentDay(startDate, startTime), safeEndCurrentDay(endDate, endTime), restaurant_id);
    }

    //tests (will be removed)

    //test string
    @GetMapping(value = ADMIN, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getTestSecurity() {
        log.info("Test security for user {}", AuthUser.id());
        return "[{\"message\":\"Access allowed\"}]";
    }

    //print data in console
    @PostMapping(value = "/date")
    public void testLocalDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime localDateTime) {
        System.out.println("Y : " + localDateTime.getYear());
        System.out.println("M: " + localDateTime.getMonth());
        System.out.println("D : " + localDateTime.getDayOfMonth());
    }
}
