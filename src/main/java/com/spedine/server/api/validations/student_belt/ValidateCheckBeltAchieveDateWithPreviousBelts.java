package com.spedine.server.api.validations.student_belt;

import com.spedine.server.domain.entity.EBelt;
import com.spedine.server.domain.entity.Student;
import com.spedine.server.domain.entity.StudentBelt;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ValidateCheckBeltAchieveDateWithPreviousBelts implements StudentBeltValidationHandler {

    @Override
    public void validate(Student student, StudentBelt newBelt) {
        // IF NEW USER, DON'T CHECK
        if (student.getBelts().isEmpty())
            return;
        List<EBelt> beltHierarchy = Arrays.stream(EBelt.values()).toList();
        List<StudentBelt> achievedBelts = student.getBelts();

        int newBeltIndex = beltHierarchy.indexOf(newBelt.getBelt().getName());

        List<StudentBelt> higherBelts = achievedBelts.stream()
                .filter(belt -> beltHierarchy.indexOf(belt.getBelt().getName()) > newBeltIndex)
                .toList();

        List<StudentBelt> lowerBelts = achievedBelts.stream()
                .filter(belt -> beltHierarchy.indexOf(belt.getBelt().getName()) < newBeltIndex)
                .toList();

        higherBelts.forEach(higherBelt -> {
            if (newBelt.getAchievedDate().isAfter(higherBelt.getAchievedDate()) || newBelt.getAchievedDate().isEqual(higherBelt.getAchievedDate())) {
                throw new ValidationException(
                        String.format("A faixa '%s' deve ter data de conquista anterior à faixa '%s'",
                                newBelt.getBelt().getName().getDescription(),
                                higherBelt.getBelt().getName().getDescription())
                );
            }
        });

        lowerBelts.forEach(lowerBelt -> {
            if (newBelt.getAchievedDate().isBefore(lowerBelt.getAchievedDate()) || newBelt.getAchievedDate().isEqual(lowerBelt.getAchievedDate())) {
                throw new ValidationException(
                        String.format("A faixa '%s' deve ter data de conquista posterior à faixa '%s'",
                                newBelt.getBelt().getName().getDescription(),
                                lowerBelt.getBelt().getName().getDescription())
                );
            }
        });
    }
}
