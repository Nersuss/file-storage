--liquibase formatted sql
--changeset Priamonosov Maksim:1.0.0

CREATE INDEX email_idx ON users (email);
