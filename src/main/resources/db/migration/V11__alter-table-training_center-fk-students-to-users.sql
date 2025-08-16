-- remove current foreign key constraint
ALTER TABLE training_centers
DROP CONSTRAINT IF EXISTS training_centers_teacher_id_fkey;

-- add new foreign key constraint
ALTER TABLE training_centers
    ADD CONSTRAINT training_centers_teacher_id_fkey
        FOREIGN KEY (teacher_id) REFERENCES users(id);