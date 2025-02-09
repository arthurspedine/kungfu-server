package com.spedine.server.api.validations.student_belt;

import com.spedine.server.domain.entity.Student;
import com.spedine.server.domain.entity.StudentBelt;

public interface StudentBeltValidationHandler {
    void validate(Student student, StudentBelt newBelt);
}
