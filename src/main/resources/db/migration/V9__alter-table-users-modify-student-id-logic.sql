ALTER TABLE users ADD COLUMN student_id UUID;

UPDATE users set student_id = id;

ALTER TABLE users
ADD CONSTRAINT fk_student_id
FOREIGN KEY (student_id) REFERENCES students (id);

ALTER TABLE users ALTER COLUMN student_id SET NOT NULL;
ALTER TABLE users DROP CONSTRAINT users_id_fkey;
