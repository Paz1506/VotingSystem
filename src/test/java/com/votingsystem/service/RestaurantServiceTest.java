package com.votingsystem.service;

import com.votingsystem.RestaurantTestData;
import com.votingsystem.entity.Restaurant;
import com.votingsystem.exceptions.EntityNotFoundException;
import com.votingsystem.repository.HibernateUtil;
import com.votingsystem.util.DateTimeUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:database/populate.sql", config = @SqlConfig(encoding = "UTF-8"))
public class RestaurantServiceTest {

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    private CacheManager cacheManager;

    @Autowired(required = false)
    private HibernateUtil hibernateUtil;

    @Before
    public void setUp() {
        cacheManager.getCache("users").clear();
        cacheManager.getCache("restaurants").clear();
        if (hibernateUtil != null) {
            hibernateUtil.clear2ndLevelHibernateCache();
        }
    }

    @Test
    public void getAll() {
        assertThat(restaurantService.getAll()).isEqualTo(RestaurantTestData.RESTAURANT_LIST);
    }

    @Test
    public void getById() throws EntityNotFoundException {
        assertThat(restaurantService.getById(1)).isEqualTo(RestaurantTestData.RESTAURANT_1);
    }

    @Test
    public void saveAndUpdate() throws EntityNotFoundException {
        Restaurant restaurant = restaurantService.getById(1);
        restaurant.setName("updated_rest_1");
        restaurantService.save(restaurant);
        Assert.assertEquals(restaurantService.getById(1).getName(), "updated_rest_1");

        Restaurant newRestaurant = new Restaurant("newRestaurant");
        restaurantService.save(newRestaurant);
        Assert.assertTrue(restaurantService.getAll().size() == RestaurantTestData.RESTAURANT_LIST.size() + 1);
    }

    @Test (expected = EntityNotFoundException.class)
    public void delete() throws EntityNotFoundException {
        restaurantService.delete(3);
        Assert.assertTrue(restaurantService.getById(3) == null);
    }

    @Test
    public void getAllRestaurantByCurrentDayWithMenu() {
        assertThat(restaurantService.getAllRestaurantByCurrentDayWithMenu(DateTimeUtil.BEGIN_CURRENT_DAY, DateTimeUtil.END_CURRENT_DAY))
                .isEqualTo(Arrays.asList(RestaurantTestData.RESTAURANT_1, RestaurantTestData.RESTAURANT_2, RestaurantTestData.RESTAURANT_3));
    }
}