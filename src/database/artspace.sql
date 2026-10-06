CREATE DATABASE IF NOT EXISTS art_space;

USE art_space;
SHOW TABLES;

CREATE TABLE IF NOT EXISTS users (
	UserId BIGINT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(100) NOT NULL,
    Email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS art_work (
	ArtworkId BIGINT PRIMARY KEY AUTO_INCREMENT,
    ArtTitle VARCHAR(100) NOT NULL,
    ArtDescription VARCHAR(100) NOT NULL,
    image_url VARCHAR(50) NOT NULL,
    FOREIGN KEY (artist_id) references artist(id),
    FOREIGN KEY (category_id) references artwork(id)
) ENGINE = InnoDB;