ALTER TABLE students
    ADD COLUMN training_center_id UUID,
ADD CONSTRAINT fk_training_centers
    FOREIGN KEY (training_center_id) 
    REFERENCES training_centers (id);