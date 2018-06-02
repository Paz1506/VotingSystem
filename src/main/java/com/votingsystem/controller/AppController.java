package com.votingsystem.controller;

import com.votingsystem.entity.*;
import com.votingsystem.exceptions.VotingTimeExpiredException;
import com.votingsystem.security.AuthUser;
import com.votingsystem.service.*;
import com.votingsystem.to.DishTo;
import com.votingsystem.to.MenuTo;
import com.votingsystem.to.UserTo;
import com.votingsystem.to.converters.DishConverter;
import com.votingsystem.to.converters.MenuConverter;
import com.votingsystem.to.converters.UserConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Основной контроллер приложения
 * Контроллер для работы с:
 * - Ресторанами
 * - Меню
 * - Блюдами
 * Создан: ZPS 2018.05.19
 */

@RestController
@RequestMapping(value = AppController.ROOT_VERSION_URL)
public class AppController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    final static String ROOT_VERSION_URL = "/v1.0";
    final static String RESTAURANTS_URL = "/restaurants";
    final static String MENUS_URL = "/menus";
    final static String DISHES_URL = "/dishes";
    final static String VOTES_URL = "/votes";
    final static String USERS_URL = "/users";
    final static String ADMIN = "/admin";

    private final static LocalTime tresholdTime = LocalTime.of(11, 0, 0);

    private final RestaurantService restaurantService;
    private final MenuService menuService;
    private final DishService dishService;
    private final VoteService voteService;
    private final UserService userService;

    @Autowired
    public AppController(RestaurantService restaurantService, MenuService menuService, DishService dishService, VoteService voteService, UserService userService) {
        this.restaurantService = restaurantService;
        this.menuService = menuService;
        this.dishService = dishService;
        this.voteService = voteService;
        this.userService = userService;
    }

    //=========================================
    //Тестовые методы
    //=========================================

    //Если имеем доступ, увидим надпись
    @GetMapping(value = ADMIN, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getTestSecurity() {
        log.info("Test security for user {}", AuthUser.id());
        return "[{\"message\":\"Access allowed\"}]";
    }

    //Тест пинятия LocalDateTime
    @PostMapping(value = "/date")
    public void testLocalDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime localDateTime) {
        System.out.println("Y : " + localDateTime.getYear());
        System.out.println("M: " + localDateTime.getMonth());
        System.out.println("D : " + localDateTime.getDayOfMonth());
    }

    //=========================================
    //Работа с ресторанами
    //=========================================

    //Вернет все рестораны
    @GetMapping(value = ADMIN + RESTAURANTS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Restaurant> getAllRestaurants() {
        log.info("User {} get list restaurants", AuthUser.id());
        return restaurantService.getAll();
    }

    //USER: Вернет все рестораны за текущий день, в которых созданы меню
    //Если меню нет, ресторана просто не будет в списке
    @GetMapping(value = RESTAURANTS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Restaurant> getAllRestaurantByCurrentDayWithMenu() {

        log.info("User {} get list restaurants by current day", AuthUser.id());

        //Конструируем начало и конец текущего дня //TODO вынести в отдельный метод
        LocalDateTime beginCurrentDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
        LocalDateTime endCurrentDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));

        return restaurantService.getAllRestaurantByCurrentDayWithMenu(beginCurrentDay, endCurrentDay);
    }

    //Создание или обновление ресторана
    @PostMapping(value = ADMIN + RESTAURANTS_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createOrUpdateRestaurant(@Valid Restaurant restaurant) {

        log.info("User {} create/update restaurant {}", AuthUser.id(), restaurant);

        restaurantService.save(restaurant);
    }

    //Вернет ресторан по id
    @GetMapping(value = {RESTAURANTS_URL + "/{id}", ADMIN + RESTAURANTS_URL + "/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Restaurant getRestaurantById(@PathVariable("id") int id) {

        log.info("User {} get restaurant {}", AuthUser.id(), id);

        return restaurantService.getById(id);
    }

    //Удалит ресторан по id
    @DeleteMapping(value = ADMIN + RESTAURANTS_URL + "/{id}")
    public void deleteRestaurantById(@PathVariable("id") int id) {
        log.info("User {} delete restaurant {}", AuthUser.id(), id);
        restaurantService.delete(id);
    }

    //Голоса за ресторан (за все даты, любыми пользователями)
    @GetMapping(value = ADMIN + RESTAURANTS_URL + "/{restaurant_id}" + VOTES_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Vote> getRestaurantVotesById(@PathVariable("restaurant_id") int restaurant_id) {
        log.info("User {} get restaurant {} votes", AuthUser.id(), restaurant_id);
        return voteService.getByRestaurantId(restaurant_id);
    }

    //USER:Вернет количество голосов за конкретный ресторан (за текущий день!)
    @GetMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + VOTES_URL + "/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public int getCountRestaurantVotesById(@PathVariable("restaurant_id") int restaurant_id) {

        //получаем текущее время
        LocalTime current_time = LocalTime.now();//Production
//        LocalTime current_time = LocalTime.of(12, 0, 0);//TODO Fix it

        if (current_time.isAfter(tresholdTime)) {
            log.info("User {} get restaurant {} count votes for current day", AuthUser.id(), restaurant_id);
            //Конструируем начало и конец текущего дня //TODO вынести в отдельный метод
            LocalDateTime beginCurrentDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
            LocalDateTime endCurrentDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));
            return voteService.getCountByRestaurantId(beginCurrentDay, endCurrentDay, restaurant_id);
        } else {
            return -1;
        }
    }

    //ADMIN:Вернет количество голосов за конкретный ресторан (за текущий день!)
    @GetMapping(value = ADMIN + RESTAURANTS_URL + "/{restaurant_id}" + VOTES_URL + "/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public int getAllCountRestaurantVotesById(
            @PathVariable("restaurant_id") int restaurant_id,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            @RequestParam(value = "startTime", required = false) LocalTime startTime,
            @RequestParam(value = "endTime", required = false) LocalTime endTime
    ) {

        //TODO сделать отдельный метод в DateTimeUtil
        //Конструируем временные промежутки, если заданы параметры,иначе текущий день
        LocalDateTime beginCurrentDay = LocalDateTime.of(startDate != null ? startDate : LocalDate.now(), startTime != null ? startTime : LocalTime.of(0, 0, 0));
        LocalDateTime endCurrentDay = LocalDateTime.of(endDate != null ? endDate : LocalDate.now(), endTime != null ? endTime : LocalTime.of(23, 59, 59));

        log.info("User {} get restaurant {} count votes between {} {} and {} {}", AuthUser.id(), restaurant_id, startDate, startTime, endDate, endTime);

        return voteService.getCountByRestaurantId(beginCurrentDay, endCurrentDay, restaurant_id);

    }

    //Вернет голос ресторана по id
    @GetMapping(value = ADMIN + RESTAURANTS_URL + "/{restaurant_id}" + VOTES_URL + "/{vote_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Vote getRestaurantVoteById(@PathVariable("vote_id") int vote_id) {
        log.info("User {} get vote {}", AuthUser.id(), vote_id);
        return voteService.getById(vote_id);
    }

    //Голосование пользователя за ресторан
    @PostMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + VOTES_URL/*, consumes = MediaType.APPLICATION_JSON_VALUE*/)
    public void createOrUpdateVote(@PathVariable("restaurant_id") int restaurant_id) throws VotingTimeExpiredException {

        //Пока сделать все проверки здесь
//        User current_user = userService.getById(16); // Захардкодил юзера 16 (в дальнейшем из секьюрити)
//        User current_user = userService.getById(AuthUser.get().getId());
        User current_user = userService.getById(AuthUser.id());

        //Конструируем начало и конец текущего дня
        LocalDateTime beginCurrentDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
        LocalDateTime endCurrentDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));

        //получаем текущее время
//        LocalTime current_time = LocalTime.now();//Production
        LocalTime current_time = LocalTime.of(10, 0, 0);//TODO Fix it

        //Если текущее время меньше порогового для голосования
        if (current_time.isBefore(tresholdTime)) {
            Vote voteOfUserByCurrentDay = voteService.getByUserIdAndDateTime(beginCurrentDay, endCurrentDay, current_user.getId());
            if (voteOfUserByCurrentDay != null) {
                log.info("User {} cancel vote for restaurant {}", current_user.getId(), restaurant_id);
                voteService.delete(voteOfUserByCurrentDay.getId());
            } else {
                log.info("User {} voting for restaurant {}", current_user.getId(), restaurant_id);
                Vote vote = new Vote();
                vote.setDateTime(LocalDateTime.now());
                vote.setRestaurant(restaurantService.getById(restaurant_id));
                vote.setUser(current_user);//Fix it!

                voteService.save(vote);
            }
        } else {
            log.info("User {} trying to vote after 11:00 for restaurant {}", AuthUser.id(), restaurant_id);
            throw new VotingTimeExpiredException();
        }
    }

    //=========================================
    //Работа с меню
    //=========================================

    //Вернет все меню ресторана за все даты
    @GetMapping(value = ADMIN + RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MenuTo> getMenusByRestaurantId(@PathVariable("restaurant_id") int restaurant_id) {
        log.info("User {} get list menus for restaurant {}", AuthUser.id(), restaurant_id);
        return menuService.getByRestaurantId(restaurant_id)
                .stream()
                .map(MenuConverter::getToFromMenu)
                .collect(Collectors.toList());
    }

    //USER: Вернет все меню ресторана за текущий день
    @GetMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MenuTo> getAllMenusOfRestaurantByCurrentDay(@PathVariable("restaurant_id") int restaurant_id) {

        log.info("User {} get list menus for restaurant {} by current day", AuthUser.id(), restaurant_id);

        //Конструируем начало и конец текущего дня //TODO вынести в отдельный метод
        LocalDateTime beginCurrentDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
        LocalDateTime endCurrentDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));

        return menuService.getAllMenusOfRestaurantByCurrentDay(restaurant_id, beginCurrentDay, endCurrentDay)
                .stream()
                .map(MenuConverter::getToFromMenu)
                .collect(Collectors.toList());
    }

    //Создание или обновление меню
    @PostMapping(value = ADMIN + RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createOrUpdateMenu(@PathVariable("restaurant_id") int restaurant_id, @Valid MenuTo menuTo) {

        log.info("User {} create/update menu {} for restaurant {}", AuthUser.id(), menuTo, restaurant_id);

        Menu menu = MenuConverter.getMenuFromTo(menuTo);
        menu.setRestaurant(restaurantService.getById(restaurant_id));
        menuService.save(menu);
    }

    //Создание или обновление меню (Сделал через параметры)
//    @PostMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL)
//    public void createOrUpdateMenu(
//            @PathVariable("restaurant_id") int restaurant_id,
//            @RequestParam("name") String name,
//            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
//        Menu menu = new Menu();
//        menu.setName(name);
//        menu.setDate(date);
//        menu.setRestaurant(restaurantService.getById(restaurant_id));
//        menuService.save(menu);
//    }

    //Вернет меню по id
    @GetMapping(value = {RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL + "/{menu_id}", ADMIN + RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL + "/{menu_id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public MenuTo getMenuById(@PathVariable("menu_id") int menu_id) {
        log.info("User {} get menu {}", AuthUser.id(), menu_id);
        return MenuConverter.getToFromMenu(menuService.getById(menu_id));
    }

    //Удалит меню по id
    @DeleteMapping(value = ADMIN + RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL + "/{menu_id}")
    public void deleteMenuById(@PathVariable("menu_id") int menu_id) {
        log.info("User {} delete menu {}", AuthUser.id(), menu_id);
        menuService.delete(menu_id);
    }

    //=========================================
    //Работа с блюдами
    //=========================================

    //вернет все блюда конкретного меню
    @GetMapping(value = ADMIN + RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL + "/{menu_id}" + DISHES_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DishTo> getDishesByMenuId(@PathVariable("menu_id") int menu_id) {
        return dishService.getByMenuId(menu_id)
                .stream()
                .map(DishConverter::getToFromDish)
                .collect(Collectors.toList());
    }

    //USER: Вернет все блюда в меню за текущий день
    @GetMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL + "/{menu_id}" + DISHES_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DishTo> getDishesOfMenuByCurrentDay(@PathVariable("menu_id") int menu_id) {
        //Конструируем начало и конец текущего дня //TODO вынести в отдельный метод

        log.info("User {} get list dishes of menu {} by current day", AuthUser.id(), menu_id);

        LocalDateTime beginCurrentDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
        LocalDateTime endCurrentDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));

        return dishService.getOfMenuByCurrentDay(menu_id, beginCurrentDay, endCurrentDay)
                .stream()
                .map(DishConverter::getToFromDish)
                .collect(Collectors.toList());
    }

    //Создание или обновление блюда
    @PostMapping(value = ADMIN + RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL + "/{menu_id}" + DISHES_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createOrUpdateDish(@PathVariable("menu_id") int menu_id, @Valid DishTo dishTo) {
        log.info("User {} create/update dish {} of menu {}", AuthUser.id(), dishTo, menu_id);
        Dish dish = DishConverter.getDishFromTo(dishTo);
        dish.setMenu(menuService.getById(menu_id));
        dishService.save(dish);
    }

    //Вернет блюдо по id
    @GetMapping(value = {RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL + "/{menu_id}" + DISHES_URL + "/{dish_id}", ADMIN + RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL + "/{menu_id}" + DISHES_URL + "/{dish_id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public DishTo getDishById(@PathVariable("dish_id") int dish_id) {
        log.info("User {} get dish {}", AuthUser.id(), dish_id);
        return DishConverter.getToFromDish(dishService.getById(dish_id));
    }

    //Удалит блюдо по id
    @DeleteMapping(value = ADMIN + RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL + "/{menu_id}" + DISHES_URL + "/{dish_id}")
    public void deleteDishById(@PathVariable("dish_id") int dish_id) {
        log.info("User {} delete dish {}", AuthUser.id(), dish_id);
        dishService.delete(dish_id);
    }

    //=========================================
    //Работа с пользователями
    //=========================================

    //Вернет всех пользователей
    @GetMapping(value = ADMIN + USERS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAllUsers() {
        log.info("User {} get list users", AuthUser.id());
        return userService.getAll();
    }

    //Создание или обновление пользователя
    @PostMapping(value = ADMIN + USERS_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createOrUpdateUser(@Valid UserTo userTo) {
        log.info("User {} create/update user {}", AuthUser.id(), userTo);
        userService.save(UserConverter.getUserFromTo(userTo));
    }

    //Вернет пользователя по id
    @GetMapping(value = ADMIN + USERS_URL + "/{user_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserById(@PathVariable("user_id") int user_id) {
        log.info("User {} get user {}", AuthUser.id(), user_id);
        return userService.getById(user_id);
    }

    //Удалит пользователя по id
    @DeleteMapping(value = ADMIN + USERS_URL + "/{user_id}")
    public void deleteUserById(@PathVariable("user_id") int user_id) {
        log.info("User {} delete user {}", AuthUser.id(), user_id);
        userService.delete(user_id);
    }

    //Вернет голоса конкретного пользователя за все время
    @GetMapping(value = ADMIN + USERS_URL + "/{user_id}" + VOTES_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Vote> getVotesByUser(@PathVariable("user_id") int user_id) {
        log.info("User {} get list votes with user {}", AuthUser.id(), user_id);
        return voteService.getByUserId(user_id);
    }

    //Вернет голос пользователя по id
    @GetMapping(value = ADMIN + USERS_URL + "/{user_id}" + VOTES_URL + "/{vote_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Vote getUserVoteById(@PathVariable("vote_id") int vote_id) {
        log.info("User {} get vote {}", AuthUser.id(), vote_id);
        return voteService.getById(vote_id);
    }


}
