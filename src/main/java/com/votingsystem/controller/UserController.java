package com.votingsystem.controller;

import com.votingsystem.entity.Dish;
import com.votingsystem.entity.Restaurant;
import com.votingsystem.entity.User;
import com.votingsystem.entity.Vote;
import com.votingsystem.exceptions.VotingTimeExpiredException;
import com.votingsystem.security.AuthUser;
import com.votingsystem.service.*;
import com.votingsystem.to.DishTo;
import com.votingsystem.to.MenuTo;
import com.votingsystem.to.converters.DishConverter;
import com.votingsystem.to.converters.MenuConverter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.votingsystem.controller.RootController.ROOT_VERSION_URL;
import static com.votingsystem.util.DateTimeUtil.*;

/**
 * @author Paz1506
 * Контроллер, обработаывающий запросы
 * пользователей системы.
 */

@RestController
@RequestMapping(value = ROOT_VERSION_URL)
public class UserController extends RootController {

    public UserController(RestaurantService restaurantService, MenuService menuService, DishService dishService, VoteService voteService, UserService userService) {
        super(restaurantService, menuService, dishService, voteService, userService);
    }

    //restaurants

    /**
     * Возвращает рестораны, в которых опубликовано меню
     * за текущий день. Если в ресторане не опубликовано
     * меню за текущий день, он не возвращается.
     *
     * @return
     */
    @GetMapping(value = RESTAURANTS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Restaurant> getAllRestaurantByCurrentDayWithMenu() {
        log.info("User {} get list restaurants by current day", AuthUser.id());
        return restaurantService.getAllRestaurantByCurrentDayWithMenu(BEGIN_CURRENT_DAY, END_CURRENT_DAY);
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

    //menus

    /**
     * Возвращает все меню ресторана
     * опубликованные за текущий день.
     *
     * @param restaurant_id
     * @return
     */
    @GetMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MenuTo> getAllMenusOfRestaurantByCurrentDay(@PathVariable("restaurant_id") int restaurant_id) {
        log.info("User {} get list menus for restaurant {} by current day", AuthUser.id(), restaurant_id);
        return menuService.getAllMenusOfRestaurantByCurrentDay(restaurant_id, BEGIN_CURRENT_DAY, END_CURRENT_DAY)
                .stream()
                .map(MenuConverter::getToFromMenu)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает меню по id для конкретного ресторана.
     *
     * @param menu_id
     * @return
     */
    @GetMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL + "/{menu_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public MenuTo getMenuById(@PathVariable("menu_id") int menu_id, @PathVariable("restaurant_id") int restaurant_id) {
        log.info("User {} get menu {}", AuthUser.id(), menu_id);
        return MenuConverter.getToFromMenu(menuService.getByIdAndRestaurantId(menu_id, restaurant_id));
    }

    //dishes

    /**
     * Возвращает все блюда в меню за текущий день.
     *
     * @param menu_id
     * @return
     */
    @GetMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL + "/{menu_id}" + DISHES_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DishTo> getDishesOfMenuByCurrentDay(@PathVariable("menu_id") int menu_id) {
        log.info("User {} get list dishes of menu {} by current day", AuthUser.id(), menu_id);
        return dishService.getOfMenuByCurrentDay(menu_id, BEGIN_CURRENT_DAY, END_CURRENT_DAY)
                .stream()
                .map(DishConverter::getToFromDish)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает блюдо по id для
     * конкретного меню и ресторана.
     *
     * @param dish_id
     * @param menu_id
     * @param restaurant_id
     * @return
     */
    @GetMapping(value = {RESTAURANTS_URL + "/{restaurant_id}" + MENUS_URL + "/{menu_id}" + DISHES_URL + "/{dish_id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public DishTo getDishById(@PathVariable("dish_id") int dish_id, @PathVariable("menu_id") int menu_id, @PathVariable("restaurant_id") int restaurant_id) {
        log.info("User {} get dish {}", AuthUser.id(), dish_id);
        return DishConverter.getToFromDish(dishService.getByRestAndMenuAndId(dish_id, menu_id, restaurant_id));
    }

    //votes

    /**
     * Возвращает количество голосов ресторана
     * за текущий день, если время больше 11:00.
     * Если время меньше - возвращает значение -1.
     *
     * @param restaurant_id
     * @return
     */
    @GetMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + VOTES_URL + "/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public int getCountRestaurantVotesById(@PathVariable("restaurant_id") int restaurant_id) {
        log.info("User {} get restaurant {} count votes for current day", AuthUser.id(), restaurant_id);
        //получаем текущее время
        LocalTime current_time = LocalTime.now();//Production
//        LocalTime current_time = LocalTime.of(12, 0, 0);//TODO Fix it
        if (current_time.isAfter(tresholdTime)) {
            return voteService.getCountByRestaurantId(BEGIN_CURRENT_DAY, END_CURRENT_DAY, restaurant_id);
        } else {
            return -1;
        }
    }

    /**
     * Голосование пользователя за ресторан. Проголосовать можно только за один
     * ресторан за текущие сутки. Голосование происходит строго до 11:00 текущих
     * суток. Голос за ресторан отменяется, если пользователь:
     * ** Голосует за другой ресторан.
     * ** Голосует за тот-же ресторан второй раз. При этом голос просто отменяется.
     * После 11:00 голосовать и отменять свой голос нельзя.
     *
     * @param restaurant_id
     * @throws VotingTimeExpiredException
     */
    @PostMapping(value = RESTAURANTS_URL + "/{restaurant_id}" + VOTES_URL/*, consumes = MediaType.APPLICATION_JSON_VALUE*/)
    public void createOrUpdateVote(@PathVariable("restaurant_id") int restaurant_id) throws VotingTimeExpiredException {
        User current_user = userService.getById(AuthUser.id());
//        LocalTime current_time = LocalTime.now();//Production
        LocalTime current_time = LocalTime.of(10, 0, 0);//TODO Fix it
        if (current_time.isBefore(tresholdTime)) {
            Vote voteOfUserByCurrentDay = voteService.getByUserIdAndDateTime(BEGIN_CURRENT_DAY, END_CURRENT_DAY, current_user.getId());
            if (voteOfUserByCurrentDay != null) {
                if (voteOfUserByCurrentDay.getRestaurant().getId() != restaurant_id) {
                    Vote vote = newUserVote(restaurant_id, current_user);
                    voteService.save(vote);
                }
                log.info("User {} cancel vote for restaurant {}", current_user.getId(), restaurant_id);
                voteService.delete(voteOfUserByCurrentDay.getId());
            } else {
                Vote vote = newUserVote(restaurant_id, current_user);
                voteService.save(vote);
            }
        } else {
            log.info("User {} trying to vote after 11:00 for restaurant {}", AuthUser.id(), restaurant_id);
            throw new VotingTimeExpiredException();
        }
    }

    /**
     * Возвращает голос авторизованного
     * пользователя за ресторан.
     *
     * @param restaurant_id
     * @param authUser
     * @return
     */
    private Vote newUserVote(int restaurant_id, User authUser) {
        log.info("User {} voting for restaurant {}", authUser.getId(), restaurant_id);
        Vote vote = new Vote();
        vote.setDateTime(LocalDateTime.now());
        vote.setRestaurant(restaurantService.getById(restaurant_id));
        vote.setUser(authUser);//Fix it!
        return vote;
    }

}
