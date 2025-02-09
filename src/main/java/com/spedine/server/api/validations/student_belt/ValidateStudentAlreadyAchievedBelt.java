package com.spedine.server.api.validations.student_belt;

import com.spedine.server.domain.entity.Student;
import com.spedine.server.domain.entity.StudentBelt;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ValidateStudentAlreadyAchievedBelt implements StudentBeltValidationHandler{
    @Override
    public void validate(Student student, StudentBelt newBelt) {
        boolean userAlreadyAchievedThisBelt = student.getBelts().stream().anyMatch(studentBelt -> studentBelt.equals(newBelt));

        if (userAlreadyAchievedThisBelt) {
            throw new ValidationException("O aluno já conquistou esta faixa.");
        }
    }
}
