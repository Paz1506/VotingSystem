DELETE FROM dish;
DELETE FROM menu;
DELETE FROM role;
DELETE FROM vote;
DELETE FROM users;
DELETE FROM restaurant;

ALTER SEQUENCE GLOBAL_SEQUENCE
RESTART WITH 10000;

INSERT INTO restaurant (id, name) VALUES
  (1, 'Restaurant_1'),
  (2, 'Restaurant_2'),
  (3, 'Restaurant_3'),
  (4, 'Restaurant_4'); --For test findAllRestaurantByCurrentDayWithMenu()

INSERT INTO menu (id, name, date, restaurant_id) VALUES
  (5, 'menu_1', now(), 1),
  (6, 'menu_2', now(), 1),
  (7, 'menu_3', now(), 2),
  (8, 'menu_4', now(), 2),
  (9, 'menu_5', now(), 3),
  (10, 'menu_6', now(), 3),
  (11, 'menu_7', '2018-05-19 10:00:00', 4); --For test findAllRestaurantByCurrentDayWithMenu()

INSERT INTO dish (id, name, price, menu_id) VALUES
  (12, 'dish_1', 1000, 5),
  (13, 'dish_2', 1500, 6),
  (14, 'dish_3', 2000, 7),
  (15, 'dish_4', 2500, 8),
  (16, 'dish_5', 3000, 9),
  (17, 'dish_6', 3500, 10),
  (18, 'dish_7', 4000, 11); --For test getDishesOfMenuByCurrentDay()

INSERT INTO users (id, name, email, password) VALUES
  (19, 'User', 'user@yandex.ru', '1234'),
  (20, 'Admin', 'admin@gmail.com', '1234'),
  (21, 'User2', 'user2@yandex.ru', '1234'),
  (22, 'User3', 'user3@yandex.ru', '1234');

INSERT INTO role (user_id, role) VALUES
  (19, 'ROLE_USER'),
  (20, 'ROLE_ADMIN'),
  (21, 'ROLE_USER'),
  (22, 'ROLE_USER');

INSERT INTO vote (id, date_time, user_id, restaurant_id) VALUES
  (23, '2018-05-19 10:00:00', 19, 1),
  (24, '2018-05-22 10:00:00', 19, 2),
  (25, '2018-05-21 10:01:00', 19, 2),
  (26, now(), 19, 3);
