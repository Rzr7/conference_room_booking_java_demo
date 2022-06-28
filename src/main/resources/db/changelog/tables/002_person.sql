--liquibase formatted sql

--changeset person:1
CREATE TABLE IF NOT EXISTS person (
    id INT AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    birthdate DATETIME NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id)
);
--rollback DROP TABLE person;