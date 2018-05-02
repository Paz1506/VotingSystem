DELETE FROM dish; -- чистим таблицу "Блюдо"
DELETE FROM menu; -- чистим таблицу "Меню"
DELETE FROM restaurant; -- чистим таблицу "Ресторан"
DELETE FROM vote; -- чистим таблицу "Голос"
DELETE FROM role; -- чистим таблицу "Роль"
DELETE FROM user; -- чистим таблицу "Пользователь"

ALTER SEQUENCE global_seq RESTART WITH 10000; -- Обнуляем последовательность

INSERT INTO restaurant (id, name) VALUES
  (1, 'Restaurant_1'),
  (2, 'Restaurant_2'),
  (3, 'Restaurant_3');

INSERT INTO menu (id, name, date, restaurant_id) VALUES
  (4, 'menu_1', now(), 1),
  (5, 'menu_2', now(), 1),
  (6, 'menu_3', now(), 2),
  (7, 'menu_3', now(), 2),
  (8, 'menu_3', now(), 3),
  (9, 'menu_3', now(), 3);

INSERT INTO dish (id, name, price, menu_id) VALUES
  (10, 'dish_1', 100, 4),
  (11, 'dish_2', 150, 5),
  (12, 'dish_3', 200, 6),
  (13, 'dish_4', 250, 7),
  (14, 'dish_5', 300, 8),
  (15, 'dish_6', 350, 9);

INSERT INTO users (id, name, email, password) VALUES
  (16, 'User', 'user@yandex.ru', '1234'),
  (17, 'Admin', 'admin@gmail.com', '1234');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 16),
  ('ROLE_ADMIN', 17);

-- INSERT INTO meals (date_time, description, calories, user_id) VALUES
--   ('2015-05-30 10:00:00', 'Завтрак', 500, 100000),
--   ('2015-05-30 13:00:00', 'Обед', 1000, 100000),
--   ('2015-05-30 20:00:00', 'Ужин', 500, 100000),
--   ('2015-05-31 10:00:00', 'Завтрак', 500, 100000),
--   ('2015-05-31 13:00:00', 'Обед', 1000, 100000),
--   ('2015-05-31 20:00:00', 'Ужин', 510, 100000),
--   ('2015-06-01 14:00:00', 'Админ ланч', 510, 100001),
--   ('2015-06-01 21:00:00', 'Админ ужин', 1500, 100001);
