package com.spedine.server.domain.service;

import com.spedine.server.api.dto.CreateStudentDTO;
import com.spedine.server.api.dto.StudentInformationDTO;
import com.spedine.server.domain.entity.Belt;
import com.spedine.server.domain.entity.Student;
import com.spedine.server.domain.entity.TrainingCenter;
import com.spedine.server.domain.repository.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StudentService {

    private final StudentRepository repository;

    private final BeltService beltService;

    private final StudentBeltService studentBeltService;

    private final TrainingCenterService trainingCenterService;

    public StudentService(StudentRepository repository, BeltService beltService, StudentBeltService studentBeltService, TrainingCenterService trainingCenterService) {
        this.repository = repository;
        this.beltService = beltService;
        this.studentBeltService = studentBeltService;
        this.trainingCenterService = trainingCenterService;
    }

    public void registerStudentByCreateDto(@Valid CreateStudentDTO dto) {
        Student student = new Student();
        TrainingCenter trainingCenter = trainingCenterService.findById(dto.trainingCenterId());
        setStudentVariables(student, dto.student(), trainingCenter);
        repository.save(student);
        Belt belt = beltService.findBeltByEnumType(dto.belt().type());
        studentBeltService.registerBeltForStudent(student, belt, dto.belt().achievedDate());
    }

    private void setStudentVariables(Student student, StudentInformationDTO dto, TrainingCenter trainingCenter) {
        student.setName(dto.name());
        student.setBirthDate(LocalDate.parse(dto.birthDate()));
        student.setSex(dto.sex());
        student.setTrainingCenter(trainingCenter);
    }
}
