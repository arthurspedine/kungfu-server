CREATE TABLE training_center
(
    id         UUID PRIMARY KEY,
    teacher_id UUID         NOT NULL,
    name       VARCHAR(150) NOT NULL,
    street     VARCHAR(150) NOT NULL,
    number     INTEGER      NOT NULL,
    city       VARCHAR(150) NOT NULL,
    state      VARCHAR(100) NOT NULL,
    zip_code   VARCHAR(8)   NOT NULL,
    FOREIGN KEY (teacher_id) REFERENCES students (id)
)