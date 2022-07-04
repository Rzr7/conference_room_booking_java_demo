--liquibase formatted sql

--changeset conference_persons:1
CREATE TABLE IF NOT EXISTS conference_persons (
    conference_id INT,
    person_id INT,
    PRIMARY KEY(conference_id, person_id)
);
--rollback DROP TABLE conference_persons;