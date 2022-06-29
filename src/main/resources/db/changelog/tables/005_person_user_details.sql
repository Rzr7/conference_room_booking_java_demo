--liquibase formatted sql

--changeset person:2
ALTER TABLE person ADD COLUMN username VARCHAR(100) NOT NULL AFTER id;
ALTER TABLE person ADD COLUMN password VARCHAR(255) NOT NULL AFTER username;
ALTER TABLE person ADD UNIQUE (username);
ALTER TABLE person ADD INDEX user_name (username);
