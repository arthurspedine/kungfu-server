package com.spedine.server.api.controller;

import com.spedine.server.api.dto.FormStudentDTO;
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
    public ResponseEntity<Void> registerStudent(@RequestBody @Valid FormStudentDTO dto, Authentication auth) {
        User user = (User) auth.getPrincipal();
        Student student = studentService.registerStudentByDto(dto, user);
        studentBeltService.registerMultipleBeltsForStudent(student, dto.belts());
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<StudentDetailsDTO> getStudentDetails(@PathVariable UUID id) {
        StudentDetailsDTO dto = studentService.getStudentDetails(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/list/all")
    public ResponseEntity<List<StudentInfoDTO>> listAllStudents(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return ResponseEntity.ok(studentService.listAll(user));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Void> editStudent(
            @PathVariable UUID id,
            @RequestBody FormStudentDTO dto,
            Authentication auth
    ) {
        User user = (User) auth.getPrincipal();
        Student student = studentService.updateStudentByDto(id, dto, user);
        studentBeltService.updateBeltsForStudent(student, dto.belts());
        return ResponseEntity.status(201).build();
    }

}
