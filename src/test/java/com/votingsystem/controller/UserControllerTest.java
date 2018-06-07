package com.votingsystem.controller;

import com.votingsystem.RestaurantTestData;
import com.votingsystem.UserTestData;
import com.votingsystem.controller.json.JsonUtil;
import com.votingsystem.entity.Dish;
import com.votingsystem.entity.Menu;
import com.votingsystem.repository.*;
import com.votingsystem.to.converters.DishConverter;
import com.votingsystem.to.converters.MenuConverter;
import com.votingsystem.util.DateTimeUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;

import static com.votingsystem.controller.RootController.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@WebAppConfiguration
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:database/populate.sql", config = @SqlConfig(encoding = "UTF-8"))
public class UserControllerTest {

    private static final CharacterEncodingFilter CHARACTER_ENCODING_FILTER = new CharacterEncodingFilter();

    static {
        CHARACTER_ENCODING_FILTER.setEncoding("UTF-8");
        CHARACTER_ENCODING_FILTER.setForceEncoding(true);
    }

    private MockMvc mockMvc;

    @Autowired
    private CacheManager cacheManager;

    @Autowired(required = false)
    private HibernateUtil hibernateUtil;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private VoteRepository voteRepository;

    @PostConstruct
    private void postConstruct() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(CHARACTER_ENCODING_FILTER)
                .apply(springSecurity())
                .build();
    }

    @Before
    public void setUp() {
        cacheManager.getCache("users").clear();
        cacheManager.getCache("restaurants").clear();
        if (hibernateUtil != null) {
            hibernateUtil.clear2ndLevelHibernateCache();
        }
    }

    @Test
    public void getAllRestaurantByCurrentDayWithMenu() throws Exception {
        mockMvc.perform(get(ROOT_VERSION_URL + RESTAURANTS_URL)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(UserTestData.USER.getEmail(), UserTestData.USER.getPassword())))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonUtil.writeIgnoreProps(restaurantRepository.findAllRestaurantByCurrentDayWithMenu(DateTimeUtil.BEGIN_CURRENT_DAY, DateTimeUtil.END_CURRENT_DAY))));
    }

    @Test
    public void getRestaurantById() throws Exception {
        mockMvc.perform(get(ROOT_VERSION_URL + RESTAURANTS_URL + "/1")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(UserTestData.USER.getEmail(), UserTestData.USER.getPassword())))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonUtil.writeValue(RestaurantTestData.RESTAURANT_1)));
    }

    @Test
    public void getAllMenusOfRestaurantByCurrentDay() throws Exception {
        mockMvc.perform(get(ROOT_VERSION_URL + RESTAURANTS_URL + "/1" + MENUS_URL)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(UserTestData.USER.getEmail(), UserTestData.USER.getPassword())))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(JsonUtil.writeIgnoreProps(menuRepository.findAllMenusOfRestaurantByCurrentDay(1, DateTimeUtil.BEGIN_CURRENT_DAY, DateTimeUtil.END_CURRENT_DAY)
                                .stream()
                                .map((MenuConverter::getToFromMenu))
                                .collect(Collectors.toList()), "date")));
    }

    @Test
    public void getMenuById() throws Exception {
        Menu menu = menuRepository.findById(5).orElse(null);
        Assert.assertEquals(menu.getName(), "menu_1");
        Assert.assertTrue(menu.getRestaurant().getId() == 1);

        mockMvc.perform(get(ROOT_VERSION_URL + RESTAURANTS_URL + "/1" + MENUS_URL + "/5")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(UserTestData.USER.getEmail(), UserTestData.USER.getPassword())))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonUtil.writeIgnoreProps(MenuConverter.getToFromMenu(menu), "date")));
    }

    @Test
    public void getDishesOfMenuByCurrentDay() throws Exception {
        mockMvc.perform(get(ROOT_VERSION_URL + RESTAURANTS_URL + "/1" + MENUS_URL + "/5" + DISHES_URL)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(UserTestData.USER.getEmail(), UserTestData.USER.getPassword())))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json(JsonUtil.writeIgnoreProps(dishRepository.findOfMenuByCurrentDay(5, DateTimeUtil.BEGIN_CURRENT_DAY, DateTimeUtil.END_CURRENT_DAY)
                                .stream()
                                .map((DishConverter::getToFromDish))
                                .collect(Collectors.toList()))));
    }

    @Test
    public void getDishById() throws Exception {
        Dish dish = dishRepository.findByRestAndMenuAndId(12, 5, 1);
        Assert.assertEquals(dish.getName(), "dish_1");
        Assert.assertTrue(dish.getPrice() == 1000);

        mockMvc.perform(get(ROOT_VERSION_URL + RESTAURANTS_URL + "/1" + MENUS_URL + "/5" + DISHES_URL + "/12")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(UserTestData.USER.getEmail(), UserTestData.USER.getPassword())))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(JsonUtil.writeIgnoreProps(DishConverter.getToFromDish(dish))));

    }

    @Test
    public void getCountRestaurantVotesById() {
        Assert.assertTrue(voteRepository.findCountByRestaurantId(DateTimeUtil.BEGIN_CURRENT_DAY, DateTimeUtil.END_CURRENT_DAY, 3) == 1);
    }

    @Test
    public void createOrUpdateVote() throws Exception {
        Assert.assertTrue(voteRepository.findCountByRestaurantId(DateTimeUtil.BEGIN_CURRENT_DAY, DateTimeUtil.END_CURRENT_DAY, 1) == 0);

        mockMvc.perform(post(ROOT_VERSION_URL + RESTAURANTS_URL + "/1" + VOTES_URL)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(UserTestData.USER.getEmail(), UserTestData.USER.getPassword())))
                .andExpect(status().isOk());

        Assert.assertTrue(voteRepository.findCountByRestaurantId(DateTimeUtil.BEGIN_CURRENT_DAY, DateTimeUtil.END_CURRENT_DAY, 1) == 1);
    }

    @Test
    public void unauthTest() throws Exception {
        mockMvc.perform(get(ROOT_VERSION_URL + RESTAURANTS_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void deniedTest() throws Exception {
        mockMvc.perform(get(ROOT_VERSION_URL + ADMIN + RESTAURANTS_URL)
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(UserTestData.USER.getEmail(), UserTestData.USER.getPassword())))
                .andExpect(status().isForbidden());
    }
}