CREATE TABLE student_belts
(
    id            UUID PRIMARY KEY,
    student_id    UUID    NOT NULL,
    belt_id       INTEGER NOT NULL,
    achieved_date DATE    NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students (id),
    FOREIGN KEY (belt_id) REFERENCES belts (id)
);