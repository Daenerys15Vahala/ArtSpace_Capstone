CREATE DATABASE IF NOT EXISTS art_space;

USE art_space;
SHOW TABLES;

CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
    ) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS categories (
                                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                          name VARCHAR(50) NOT NULL UNIQUE
    ) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS artworks (
                                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                        title VARCHAR(150) NOT NULL,
    description VARCHAR(1000),
    image_path VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
    ) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS favorites (
                                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                         user_id BIGINT NOT NULL,
                                         artwork_id BIGINT NOT NULL,

                                         FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (artwork_id) REFERENCES artworks(id),

    UNIQUE (user_id, artwork_id)
    ) ENGINE = InnoDB;

INSERT INTO categories (name) VALUES
                                  ('Painting'),
                                  ('Drawing'),
                                  ('Photography'),
                                  ('Digital Art');

SELECT * FROM categories;

INSERT INTO users (name, email, password, role)
VALUES ('Hal Jordan', 'halj@example.com', 'test-password', 'USER');
SELECT * FROM users;

INSERT INTO artworks (title, description, image_path, created_at, user_id, category_id)
VALUES ('Funeral Green', 'A green painting', '/uploads/green/jpg', NOW(), 1, 1);
SELECT * FROM artworks;

INSERT INTO favorites (user_id, artwork_id)
VALUES (1, 1);
SELECT * FROM favorites;

SELECT u.name AS user_name,
       a.title AS favorite_artwork
FROM favorites f
         JOIN users u ON f.user_id = u.id
         JOIN artworks a ON f.artwork_id = a.id;

SELECT id, name, email, password, role
FROM users
WHERE email = 'hal@example.com';

UPDATE users
SET role = 'ADMIN'
WHERE email = 'admin@artspace.com';

SELECT id, name, email, role
FROM users
WHERE email = 'admin@artspace.com';

SELECT id, title, description, image_path, user_id, category_id
FROM artworks;

SELECT id, name, email, role
FROM users
WHERE email = 'taylor.artist@example.com';

SELECT id, name, email, role
FROM users;

SELECT id, title, image_path
FROM artworks;

DELETE FROM artworks
WHERE id = 1
  AND title = 'Should Not Update'
  AND image_path = 'uploads/sunset.jpg';

DELETE FROM artworks
WHERE id = 1
  AND title = 'Should Not Update'
  AND image_path = 'uploads/sunset.jpg';

SELECT id, title, image_path
FROM artworks;

SELECT *
FROM favorites
WHERE artwork_id = 1;

DELETE FROM favorites
WHERE artwork_id = 1;

DELETE FROM artworks
WHERE id = 1
  AND title = 'Should Not Update'
  AND image_path = 'uploads/sunset.jpg';

SELECT id, title, description, image_path, category_id, user_id
FROM artworks
ORDER BY id;

SELECT id, title, image_path
FROM artworks
ORDER BY id;

DELETE FROM artworks
WHERE id IN (3, 4, 5, 6, 7, 8, 9, 10, 11, 12);