--liquibase formatted sql
--changeset Priamonosov Maksim:1.0.0

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    CONSTRAINT email UNIQUE(email)
);
