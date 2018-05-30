package com.votingsystem.controller;

import com.votingsystem.entity.*;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    private final Logger logger = LoggerFactory.getLogger(getClass());

    final static String ROOT_VERSION_URL = "/v1.0";
    final static String RESTAURANTS_URL = "/restaurants";
    final static String MENUS_URL = "/menus";
    final static String DISHES_URL = "/dishes";
    final static String VOTES_URL = "/votes";
    final static String USERS_URL = "/users";

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
    @GetMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getTestSecurity() {
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
    @GetMapping(value = RESTAURANTS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.getAll();
    }

    //Создание или обновление ресторана
    @PostMapping(value = RESTAURANTS_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createOrUpdateRestaurant(@Valid Restaurant restaurant) {
        restaurantService.save(restaurant);
    }

    //Вернет ресторан по id
    @GetMapping(value = RESTAURANTS_URL + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Restaurant getRestaurantById(@PathVariable("id") int id) {
        return restaurantService.getById(id);
    }

    //Удалит ресторан по id
    @DeleteMapping(value = RESTAURANTS_URL + "/{id}")
    public void deleteRestaurantById(@PathVariable("id") int id) {
        restaurantService.delete(id);
    }

    //Голоса за ресторан (за все даты, любыми пользователями)
    @GetMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + VOTES_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Vote> getRestaurantVotesById(@PathVariable("restaurant_id") int restaurant_id) {
        return voteService.getByRestaurantId(restaurant_id);
    }

    //Вернет голос ресторана по id
    @GetMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + VOTES_URL + "/{vote_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Vote getRestaurantVoteById(@PathVariable("vote_id") int vote_id) {
        return voteService.getById(vote_id);
    }

    //Голосование пользователя за ресторан TODO прикрутить секьюрити
    @PostMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + VOTES_URL/*, consumes = MediaType.APPLICATION_JSON_VALUE*/)
    public void createOrUpdateVote(@PathVariable("restaurant_id") int restaurant_id) {

        //Пока сделать все проверки здесь
        User current_user = userService.getById(16); // Захардкодил юзера 16 (в дальнейшем из секьюрити)

        //Конструируем начало и конец текущего дня
        LocalDateTime beginCurrentDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
        LocalDateTime endCurrentDay = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));

        //получаем текущее время
        LocalTime current_time = LocalTime.now();//Production
//        LocalTime current_time = LocalTime.of(12,0,0);//Fix it

        //Если текущее время меньше порогового для голосования
        if (current_time.isBefore(tresholdTime)){
            Vote voteOfUserByCurrentDay = voteService.getByUserIdAndDateTime(beginCurrentDay, endCurrentDay, 16);
            if (voteOfUserByCurrentDay!=null){
                voteService.delete(voteOfUserByCurrentDay.getId());
            } else {

                Vote vote = new Vote();
                vote.setDateTime(LocalDateTime.now());
                vote.setRestaurant(restaurantService.getById(restaurant_id));
                vote.setUser(current_user);//Fix it!

                voteService.save(vote);
            }
        }
    }

    //=========================================
    //Работа с меню
    //=========================================

    //Вернет все меню ресторана за все даты
    @GetMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MenuTo> getMenusByRestaurantId(@PathVariable("restaurant_id") int restaurant_id) {
        return menuService.getByRestaurantId(restaurant_id).stream().map(MenuConverter::getToFromMenu).collect(Collectors.toList());
    }

    //TODO ошибка биндинга даты если принимаем ТО, если Entity, все нормально
    //Создание или обновление меню
    @PostMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL)
    public void createOrUpdateMenu(@PathVariable("restaurant_id") int restaurant_id, @Valid MenuTo menuTo) {
        System.out.println("!!!!!!!!!!!! " + menuTo);
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
    @GetMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL + "/{menu_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public MenuTo getMenuById(@PathVariable("menu_id") int menu_id) {
        return MenuConverter.getToFromMenu(menuService.getById(menu_id));
    }

    //Удалит меню по id
    @DeleteMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL + "/{menu_id}")
    public void deleteMenuById(@PathVariable("menu_id") int id) {
        menuService.delete(id);
    }

    //=========================================
    //Работа с блюдами
    //=========================================

    //вернет все блюда конкретного меню
    //Переделал под ТО
    @GetMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL + "/{menu_id}" + DISHES_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DishTo> getDishesByMenuId(@PathVariable("menu_id") int menu_id) {
        return dishService.getByMenuId(menu_id).stream().map(DishConverter::getToFromDish).collect(Collectors.toList());
    }

    //Создание или обновление блюда
    @PostMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL + "/{menu_id}" + DISHES_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createOrUpdateDish(@PathVariable("menu_id") int menu_id, @Valid DishTo dishTo) {
        Dish dish = DishConverter.getDishFromTo(dishTo);
        dish.setMenu(menuService.getById(menu_id));
        dishService.save(dish);
    }

    //Вернет блюдо по id
    @GetMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL + "/{menu_id}" + DISHES_URL + "/{dish_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DishTo getDishById(@PathVariable("dish_id") int dish_id) {
        return DishConverter.getToFromDish(dishService.getById(dish_id));
    }

    //Удалит блюдо по id
    @DeleteMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL + "/{menu_id}" + DISHES_URL + "/{dish_id}")
    public void deleteDishById(@PathVariable("dish_id") int id) {
        dishService.delete(id);
    }

    //=========================================
    //Работа с пользователями
    //=========================================

    //Вернет всех пользователей
    @GetMapping(value = USERS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAllUsers() {
        return userService.getAll();
    }

//    //Создание или обновление пользователя
//    @PostMapping(value = USERS_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public void createOrUpdateUser(@Valid User user) {
//        //TODO будет тоько обычный пользователь (@JsonIgnore roles). Исправить через ТО
//        Set<Role> roles = new HashSet<>();
//        roles.add(Role.ROLE_USER);
//        user.setRoles(roles);
//        userService.save(user);
//    }

    //Создание или обновление пользователя
    @PostMapping(value = USERS_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createOrUpdateUser(@Valid UserTo userTo) {
        userService.save(UserConverter.getUserFromTo(userTo));
    }


    //Вернет пользователя по id
    @GetMapping(value = USERS_URL + "/{user_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserById(@PathVariable("user_id") int user_id) {
        return userService.getById(user_id);
    }

    //Удалит пользователя по id
    @DeleteMapping(value = USERS_URL + "/{user_id}")
    public void deleteUserById(@PathVariable("user_id") int user_id) {
        userService.delete(user_id);
    }

    //Вернет голоса конкретного пользователя за все время
    @GetMapping(value = USERS_URL + "/{user_id}" + VOTES_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Vote> getVotesByUser(@PathVariable("user_id") int user_id) {
        return voteService.getByUserId(user_id);
    }

    //Вернет голос пользователя по id
    @GetMapping(value = USERS_URL + "/{user_id}" + VOTES_URL + "/{vote_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Vote getUserVoteById(@PathVariable("vote_id") int vote_id) {
        return voteService.getById(vote_id);
    }


}
