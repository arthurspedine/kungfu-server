package com.spedine.server.domain.service;

import com.spedine.server.api.dto.CreateStudentDTO;
import com.spedine.server.api.dto.StudentInformationDTO;
import com.spedine.server.domain.entity.Student;
import com.spedine.server.domain.entity.TrainingCenter;
import com.spedine.server.domain.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentRepository repository;

    private final TrainingCenterService trainingCenterService;

    public StudentService(StudentRepository repository, TrainingCenterService trainingCenterService) {
        this.repository = repository;
        this.trainingCenterService = trainingCenterService;
    }

    public Student registerStudentByCreateDto(@Valid CreateStudentDTO dto) {
        Student student = new Student();
        TrainingCenter trainingCenter = trainingCenterService.findById(dto.trainingCenterId());
        setStudentVariables(student, dto.student(), trainingCenter);
        return repository.save(student);
    }

    public Student findStudentById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Estudante nao encontrado."));
    }

    private void setStudentVariables(Student student, StudentInformationDTO dto, TrainingCenter trainingCenter) {
        student.setName(dto.name());
        student.setBirthDate(LocalDate.parse(dto.birthDate()));
        student.setSex(dto.sex());
        student.setTrainingCenter(trainingCenter);
    }
}
