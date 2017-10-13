DROP TABLE IF EXISTS users CASCADE ;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS restaurants CASCADE ;
DROP TABLE IF EXISTS orders CASCADE ;
DROP TABLE IF EXISTS menu_lists CASCADE ;
DROP TABLE IF EXISTS dishes CASCADE ;
DROP TABLE IF EXISTS orders_dishes ;

DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START 100000;

CREATE TABLE users
(
  id                  INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
  name                VARCHAR NOT NULL,
  email               VARCHAR NOT NULL UNIQUE,
  password            VARCHAR NOT NULL,
  enabled             BOOL DEFAULT TRUE,
  totalOrdersAmount  DOUBLE PRECISION
);

CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE roles
(
  user_id INTEGER NOT NULL,
  role    VARCHAR,
  CONSTRAINT user_roles_idx UNIQUE (user_id, role),
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE restaurants
(
  id         INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
  name       VARCHAR NOT NULL,
  address    VARCHAR NOT NULL
);

CREATE TABLE orders(
  id              INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
  user_id         INTEGER NOT NULL ,
  restaurant_id  INTEGER NOT NULL ,
  date_time      TIMESTAMP NOT NULL,
  status         VARCHAR,
  total_price    DOUBLE PRECISION,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ,
  FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
);

CREATE TABLE menu_lists(
  id              INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
  description    VARCHAR NOT NULL,
  date_time      TIMESTAMP NOT NULL,
  restaurant_id INTEGER NOT NULL ,
  enabled    BOOL DEFAULT TRUE,
  FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE
);

CREATE TABLE dishes
(
  id                INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
  description      VARCHAR NOT NULL,
  price             DOUBLE PRECISION,
  menu_list_id     INTEGER NOT NULL ,
  FOREIGN KEY (menu_list_id) REFERENCES menu_lists (id) ON DELETE CASCADE
);

CREATE TABLE orders_dishes
(
  order_id         INTEGER NOT NULL,
  dish_id          INTEGER NOT NULL,
  dish_quantity   INTEGER NOT NULL,
  PRIMARY KEY (order_id,dish_id),
  FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
  FOREIGN KEY (dish_id) REFERENCES dishes(id) ON DELETE CASCADE
);







