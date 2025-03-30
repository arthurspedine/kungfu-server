package com.spedine.server.api.controller;

import com.spedine.server.api.dto.CreateStudentDTO;
import com.spedine.server.domain.entity.Student;
import com.spedine.server.domain.entity.User;
import com.spedine.server.domain.service.StudentBeltService;
import com.spedine.server.domain.service.StudentService;
import com.spedine.server.dto.StudentDetailsDTO;
import com.spedine.server.dto.StudentInfoDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<?> registerStudent(@RequestBody @Valid CreateStudentDTO dto, Authentication auth) {
        User user = (User) auth.getPrincipal();
        Student student = studentService.registerStudentByCreateDto(dto, user);
        studentBeltService.registerMultipleBeltsForStudent(student, dto.belts());
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<StudentDetailsDTO> studentInfo(@PathVariable UUID id) {
        StudentDetailsDTO dto = studentService.getStudentInfoById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/list/all")
    public ResponseEntity<List<StudentInfoDTO>> listAllStudents(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return ResponseEntity.ok(studentService.listAll(user));
    }

}
