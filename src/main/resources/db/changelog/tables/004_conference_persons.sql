--liquibase formatted sql

--changeset conference_persons:1
CREATE TABLE IF NOT EXISTS conference_persons (
    conference_id INT,
    person_id INT,
    PRIMARY KEY(conference_id, person_id),
    CONSTRAINT fk_conference_person_person FOREIGN KEY (person_id) REFERENCES person (id),
    CONSTRAINT fk_conference_person_conference FOREIGN KEY (conference_id) REFERENCES conference (id)
);
--rollback DROP TABLE conference_persons;