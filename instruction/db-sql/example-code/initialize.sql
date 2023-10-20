DROP DATABASE pet_store;
CREATE DATABASE pet_store;

USE pet_store;

CREATE TABLE pet (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    INDEX (name)
);
