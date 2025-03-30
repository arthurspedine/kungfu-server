package com.spedine.server.api.controller;

import com.spedine.server.api.dto.AddBeltToStudent;
import com.spedine.server.domain.entity.Student;
import com.spedine.server.domain.entity.StudentBelt;
import com.spedine.server.domain.service.StudentBeltService;
import com.spedine.server.domain.service.StudentService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student-belt")
public class StudentBeltController {

    private final StudentBeltService service;
    private final StudentService studentService;

    public StudentBeltController(StudentBeltService service, StudentService studentService) {
        this.service = service;
        this.studentService = studentService;
    }

    @PostMapping("/add")
    @Transactional
    public ResponseEntity<?> addBeltToStudent(@RequestBody AddBeltToStudent dto) {
        Student student = studentService.findStudentById(dto.studentId());
        StudentBelt studentBelt = service.registerBeltForStudent(student, dto.belt());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.mapperToDTO(studentBelt));
    }
}
