--liquibase formatted sql

--changeset room:1
CREATE TABLE IF NOT EXISTS room (
    id INT AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(255) NOT NULL,
    capacity INT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id)
);
--rollback DROP TABLE room;