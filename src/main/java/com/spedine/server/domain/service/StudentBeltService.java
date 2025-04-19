package com.spedine.server.domain.service;

import com.spedine.server.api.dto.BeltDataDTO;
import com.spedine.server.domain.entity.EBelt;
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
import java.util.Set;
import java.util.stream.Collectors;

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
        saveBeltsDtoToStudent(student, dto);
    }

    public StudentBeltDTO mapperToDTO(StudentBelt studentBelt) {
        return new StudentBeltDTO(
                studentBelt.getId(), studentBelt.getStudent().getId(),
                new BeltInfoDTO(studentBelt.getBelt().getName().getDescription(),
                studentBelt.getAchievedDate().toString()));
    }

    @Transactional
    public void updateBeltsForStudent(Student student, List<BeltDataDTO> dto) {
        Set<EBelt> requestedBeltTypes = dto.stream()
                .map(BeltDataDTO::type)
                .collect(Collectors.toSet());

        List<StudentBelt> currentBelts = new ArrayList<>(student.getBelts());

        List<StudentBelt> beltsToRemove = currentBelts.stream()
                .filter(studentBelt -> !requestedBeltTypes.contains(studentBelt.getBelt().getName()))
                .toList();

        if (!beltsToRemove.isEmpty()) {
            student.getBelts().removeAll(beltsToRemove);
            repository.deleteAll(beltsToRemove);
        }

        Set<EBelt> existingBeltTypes = currentBelts.stream()
                .map(sb -> sb.getBelt().getName())
                .collect(Collectors.toSet());

        List<BeltDataDTO> beltsToAdd = dto.stream()
                .filter(belt -> !existingBeltTypes.contains(belt.type()))
                .toList();

        if (!beltsToAdd.isEmpty()) {
            saveBeltsDtoToStudent(student, beltsToAdd);
        }
    }

    private void saveBeltsDtoToStudent(Student student, List<BeltDataDTO> newBelts) {
        List<StudentBelt> beltsToSave = new ArrayList<>();
        for (BeltDataDTO beltDTO : newBelts) {
            Belt belt = beltService.findBeltByEnumType(beltDTO.type());
            LocalDate parsedAchievedDate = LocalDate.parse(beltDTO.achievedDate());
            StudentBelt studentBelt = new StudentBelt(student, belt, parsedAchievedDate);
            validations.forEach(validation -> validation.validate(student, studentBelt));
            student.getBelts().add(studentBelt);
            beltsToSave.add(studentBelt);
        }
        repository.saveAll(beltsToSave);
    }
}
