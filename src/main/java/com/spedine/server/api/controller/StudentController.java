package com.spedine.server.api.controller;

import com.spedine.server.api.dto.CreateStudentDTO;
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

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> registerStudent(@RequestBody @Valid CreateStudentDTO dto) {
        studentService.registerStudentByCreateDto(dto);
        return ResponseEntity.status(201).build();
    }

}
