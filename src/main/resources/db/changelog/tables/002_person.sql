--liquibase formatted sql

--changeset person:1
CREATE TABLE IF NOT EXISTS person (
    id INT AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    birthdate DATETIME NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id),
    UNIQUE(username),
    INDEX user_name(username)
);
--rollback DROP TABLE person;