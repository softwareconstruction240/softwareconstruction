DROP DATABASE pets;
CREATE DATABASE pets;

USE pets;

CREATE TABLE pet (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    INDEX (name)
);


CREATE TABLE owner (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    phoneNumber VARCHAR(255),
    PRIMARY KEY (id),
    INDEX (name)
);


CREATE TABLE purchase (
    id INT NOT NULL AUTO_INCREMENT,
    ownerId INT NOT NULL,
    petId INT NOT NULL,
    price INT NOT NULL,
    PRIMARY KEY (id),
    INDEX (ownerId),
    INDEX (petId)
);

INSERT INTO pet (name, type) VALUES ("Fido", "dog");
INSERT INTO pet (name, type) VALUES ("Puddles", "cat");
INSERT INTO pet (name, type) VALUES ("Chip", "bird");
INSERT INTO owner (name, phoneNumber) VALUES ("Juan", "801-866-3333";
INSERT INTO owner (name, phoneNumber) VALUES ("Pat", "619-583-9923");
INSERT INTO owner (name, phoneNumber) VALUES ("Tessa", "217-360-3168");
INSERT INTO purchase (ownerId, petId, price) VALUES(1, 1, 600);
INSERT INTO purchase (ownerId, petId, price) VALUES(2, 2, 20);
INSERT INTO purchase (ownerId, petId, price) VALUES(2, 3, 5);

SELECT purchase.id, purchase.ownerId, pet.id, pet.name, pet.type FROM purchase JOIN pet WHERE purchase.petId = pet.id;

