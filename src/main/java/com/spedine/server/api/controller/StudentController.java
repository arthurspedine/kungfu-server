package com.spedine.server.api.controller;

import com.spedine.server.api.dto.CreateStudentDTO;
import com.spedine.server.domain.entity.Student;
import com.spedine.server.domain.service.StudentBeltService;
import com.spedine.server.domain.service.StudentService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private final StudentBeltService studentBeltService;

    public StudentController(StudentService studentService, StudentBeltService studentBeltService) {
        this.studentService = studentService;
        this.studentBeltService = studentBeltService;
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> registerStudent(@RequestBody @Valid CreateStudentDTO dto) {
        Student student = studentService.registerStudentByCreateDto(dto);
        studentBeltService.registerBeltForStudent(student, dto.belt());
        return ResponseEntity.status(201).build();
    }

}
