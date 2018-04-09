DROP TABLE dish IF EXISTS;
DROP TABLE menu IF EXISTS;
DROP TABLE restaurant IF EXISTS;
DROP TABLE role IF EXISTS;
DROP TABLE user IF EXISTS;
DROP TABLE vote IF EXISTS;
DROP SEQUENCE GLOBAL_SEQUENCE IF EXISTS;

CREATE SEQUENCE GLOBAL_SEQUENCE
  AS INTEGER
  START WITH 10000;

-- Таблица "Блюдо"
CREATE TABLE dish
(
  id               INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQUENCE PRIMARY KEY,
  name             VARCHAR(100)            NOT NULL,
  price            VARCHAR(255)            NOT NULL,
  menu_id          INT                     NOT NULL,
  FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE --При удалении меню - уд. все блюда в нем
);

-- Таблица "Меню"
CREATE TABLE menu
(
  id               INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQUENCE PRIMARY KEY,
  date             TIMESTAMP               NOT NULL,
  restaurant_id    INT                     NOT NULL,
  FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE --При удалении ресторана, уд. все меню.
);

-- Таблица "Ресторан"
CREATE TABLE restaurant
(
  id               INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQUENCE PRIMARY KEY,
  name             VARCHAR(255)            NOT NULL
);

-- Таблица "Роль"
CREATE TABLE role
(
  id               INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQUENCE PRIMARY KEY,
  user_id INTEGER                          NOT NULL,
  role    VARCHAR(255),
  CONSTRAINT user_roles_idx UNIQUE (user_id, role),
  FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE
);

-- Таблица "Пользователь"
CREATE TABLE user
(
  id               INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQUENCE PRIMARY KEY,
  name             VARCHAR(255)            NOT NULL,
  email            VARCHAR(255)            NOT NULL,
  password         VARCHAR(255)            NOT NULL,
  registered       TIMESTAMP DEFAULT now() NOT NULL,
  enabled          BOOLEAN DEFAULT TRUE    NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON USER (email);

-- Таблица "Голос"
CREATE TABLE vote
(
  id               INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQUENCE PRIMARY KEY,
  datetime       TIMESTAMP DEFAULT now()   NOT NULL,
  user_id INT                              NOT NULL,
  CONSTRAINT votes_idx UNIQUE (user_id, datetime),
  FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE
);