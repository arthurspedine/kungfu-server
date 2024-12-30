ALTER TABLE students
    ADD COLUMN training_center_id UUID,
ADD CONSTRAINT fk_training_center 
    FOREIGN KEY (training_center_id) 
    REFERENCES training_center (id);