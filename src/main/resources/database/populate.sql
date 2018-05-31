DELETE FROM dish; -- чистим таблицу "Блюдо"
DELETE FROM menu; -- чистим таблицу "Меню"
DELETE FROM restaurant; -- чистим таблицу "Ресторан"
DELETE FROM vote; -- чистим таблицу "Голос"
DELETE FROM role; -- чистим таблицу "Роль"
DELETE FROM users; -- чистим таблицу "Пользователь"

ALTER SEQUENCE GLOBAL_SEQUENCE
RESTART WITH 10000; -- Обнуляем последовательность

INSERT INTO restaurant (id, name) VALUES
  (1, 'Restaurant_1'),
  (2, 'Restaurant_2'),
  (3, 'Restaurant_3'),
  (978, 'Restaurant_978'); --For test findAllRestaurantByCurrentDayWithMenu()

INSERT INTO menu (id, name, date, restaurant_id) VALUES
  (4, 'menu_1', now(), 1),
  (5, 'menu_2', now(), 1),
  (6, 'menu_3', now(), 2),
  (7, 'menu_4', now(), 2),
  (8, 'menu_5', now(), 3),
  (9, 'menu_6', now(), 3),
  (977, 'menu_7', '2018-05-19 10:00:00', 978); --For test findAllRestaurantByCurrentDayWithMenu()

INSERT INTO dish (id, name, price, menu_id) VALUES
  (10, 'dish_1', 1000, 4),
  (11, 'dish_2', 1500, 5),
  (12, 'dish_3', 2000, 6),
  (13, 'dish_4', 2500, 7),
  (14, 'dish_5', 3000, 8),
  (15, 'dish_6', 3500, 9),
  (979, 'dish_7', 3500, 977); --For test getDishesOfMenuByCurrentDay()

INSERT INTO users (id, name, email, password) VALUES
  (16, 'User', 'user@yandex.ru', '1234'),
  (17, 'Admin', 'admin@gmail.com', '1234'),
  (22, 'User2', 'user2@yandex.ru', '1234'),
  (23, 'User3', 'user3@yandex.ru', '1234');

INSERT INTO role (user_id, role) VALUES
  (16, 'ROLE_USER'),
  (17, 'ROLE_ADMIN'),
  (22, 'ROLE_USER'),
  (23, 'ROLE_USER');

INSERT INTO vote (id, date_time, user_id, restaurant_id) VALUES
  --Так ошибка: SQLDataException: data exception: invalid datetime format
  (18, '2018-05-19 10:00:00', 16, 1),
  (19, '2018-05-22 10:00:00', 16, 2),
  (20, '2018-05-21 10:00:00', 16, 2),
  (21, '2018-05-20 10:00:00', 16, 3);
--   Убрал кнстрейнт временно
/*  (18, now(), 16, 1),
  (19, now(), 17, 2),
  (20, now(), 22, 1),
  (21, now(), 23, 3);*/
