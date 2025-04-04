package com.spedine.server.domain.service;

import com.spedine.server.api.dto.BeltDataDTO;
import com.spedine.server.domain.validations.student_belt.StudentBeltValidationHandler;
import com.spedine.server.domain.entity.Belt;
import com.spedine.server.domain.entity.Student;
import com.spedine.server.domain.entity.StudentBelt;
import com.spedine.server.domain.repository.StudentBeltRepository;
import com.spedine.server.dto.BeltInfoDTO;
import com.spedine.server.dto.StudentBeltDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentBeltService {

    private final StudentBeltRepository repository;

    private final BeltService beltService;

    @Autowired
    private List<StudentBeltValidationHandler> validations;

    public StudentBeltService(StudentBeltRepository repository, BeltService beltService) {
        this.repository = repository;
        this.beltService = beltService;
    }

    @Transactional
    public StudentBelt registerBeltForStudent(Student student, BeltDataDTO dto)  {
        Belt belt = beltService.findBeltByEnumType(dto.type());
        LocalDate parsedAchievedDate = LocalDate.parse(dto.achievedDate());
        StudentBelt studentBelt = new StudentBelt(student, belt, parsedAchievedDate);

        validations.forEach(validation -> validation.validate(student, studentBelt));

        return repository.save(studentBelt);
    }

    @Transactional
    public void registerMultipleBeltsForStudent(Student student, List<BeltDataDTO> dto)  {
        List<StudentBelt> belts = new ArrayList<>();
        for (BeltDataDTO beltDTO : dto) {
            Belt belt = beltService.findBeltByEnumType(beltDTO.type());
            LocalDate parsedAchievedDate = LocalDate.parse(beltDTO.achievedDate());
            StudentBelt studentBelt = new StudentBelt(student, belt, parsedAchievedDate);
            validations.forEach(validation -> validation.validate(student, studentBelt));
            student.getBelts().add(studentBelt); // update student for each validation
            belts.add(studentBelt);
        }
        repository.saveAll(belts);
    }

    public StudentBeltDTO mapperToDTO(StudentBelt studentBelt) {
        return new StudentBeltDTO(
                studentBelt.getId(), studentBelt.getStudent().getId(),
                new BeltInfoDTO(studentBelt.getBelt().getName().getDescription(),
                studentBelt.getAchievedDate().toString()));
    }
}
