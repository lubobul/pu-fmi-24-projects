--Here goes all DDL

CREATE TABLE IF NOT EXISTS td_users
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    is_active   BOOLEAN DEFAULT TRUE,
    first_name  VARCHAR(256),
    last_name   VARCHAR(256),
    email       VARCHAR(256),
    phone       VARCHAR(64),
    personal_id VARCHAR(256),
    address VARCHAR(256),
    UNIQUE(email),
    UNIQUE(personal_id),
    UNIQUE(phone)
);

CREATE TABLE IF NOT EXISTS td_employees
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    is_active BOOLEAN DEFAULT TRUE,
    position  VARCHAR(256),
    user_id   INT,
    FOREIGN KEY (user_id) REFERENCES td_users (id)
);

CREATE TABLE IF NOT EXISTS td_customers
(
    id                 INT PRIMARY KEY AUTO_INCREMENT,
    is_active          BOOLEAN DEFAULT TRUE,
    has_past_accidents BOOLEAN DEFAULT FALSE,
    age INT,
    city_id            INT,
    user_id            INT,
    FOREIGN KEY (user_id) REFERENCES td_users (id)
);

CREATE TABLE IF NOT EXISTS td_cities
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    is_active   BOOLEAN DEFAULT TRUE,
    postal_code VARCHAR(64),
    name        VARCHAR(256),
    UNIQUE(name),
    UNIQUE(postal_code)
);

CREATE TABLE IF NOT EXISTS td_cars
(
    id                INT PRIMARY KEY AUTO_INCREMENT,
    is_active         BOOLEAN DEFAULT TRUE,
    model_year              INT,
    model             VARCHAR(256),
    brand             VARCHAR(256),
    kilometers_driven INT,
    price_per_day     DOUBLE,
    city_id INT,
    FOREIGN KEY (city_id) REFERENCES td_cities (id)
    );

CREATE TABLE IF NOT EXISTS td_offers
(
    id               INT PRIMARY KEY AUTO_INCREMENT,
    is_active        BOOLEAN DEFAULT TRUE,
    date_created     TIMESTAMP,
    date_accepted    TIMESTAMP    DEFAULT NULL,
    requested_from   TIMESTAMP,
    requested_to     TIMESTAMP,
    rejected         BOOL    DEFAULT FALSE,
    calculated_price DOUBLE,
    customer_id      INT,
    FOREIGN KEY (customer_id) REFERENCES td_customers (id),
    employee_id      INT,
    FOREIGN KEY (employee_id) REFERENCES td_employees (id),
    car_id           INT,
    FOREIGN KEY (car_id) REFERENCES td_cars (id),
    city_id          INT,
    FOREIGN KEY (city_id) REFERENCES td_cities (id)
);

CREATE TABLE IF NOT EXISTS td_expense_items
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    is_active   BOOLEAN DEFAULT TRUE,
    offer_id    INT,
    type        VARCHAR(64),
    price       DOUBLE,
    days        INT    DEFAULT 0,
    FOREIGN KEY (offer_id) REFERENCES td_offers (id)
);

DROP TABLE td_expense_items;
DROP TABLE td_offers;
DROP TABLE td_customers;
DROP TABLE td_employees;
DROP TABLE td_users;
DROP TABLE td_cars;
DROP TABLE td_cities;