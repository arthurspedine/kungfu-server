CREATE TABLE students
(
    id                 UUID PRIMARY KEY,
    name               VARCHAR(255) NOT NULL,
    birth_date         DATE         NOT NULL,
    sex                VARCHAR(20)  NOT NULL CHECK (sex IN ('M', 'F')),
    active             BOOLEAN DEFAULT TRUE NOT NULL
);