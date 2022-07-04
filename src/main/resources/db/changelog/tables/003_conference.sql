--liquibase formatted sql

--changeset conference:1
CREATE TABLE IF NOT EXISTS conference (
    id INT AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    room_id INT NOT NULL,
    owner_id INT NOT NULL,
    booked_at DATETIME NOT NULL,
    duration INT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id)
);
--rollback DROP TABLE conference;