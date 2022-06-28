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
    PRIMARY KEY(id),
    CONSTRAINT fk_conference_room FOREIGN KEY (room_id) REFERENCES room (id),
    CONSTRAINT fk_conference_owner FOREIGN KEY (owner_id) REFERENCES person (id)
);
--rollback DROP TABLE conference;