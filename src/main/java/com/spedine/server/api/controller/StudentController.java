package com.spedine.server.api.controller;

import com.spedine.server.api.dto.FormStudentDTO;
import com.spedine.server.api.dto.PageDTO;
import com.spedine.server.domain.entity.Student;
import com.spedine.server.domain.entity.User;
import com.spedine.server.domain.service.StudentBeltService;
import com.spedine.server.domain.service.StudentService;
import com.spedine.server.dto.StudentDetailsDTO;
import com.spedine.server.dto.StudentInfoDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/student")
@SecurityRequirement(name = "bearer-key")
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
    public ResponseEntity<PageDTO<StudentInfoDTO>> listAllStudents(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<StudentInfoDTO> pageResult = studentService.listAll(user, pageable);
        return ResponseEntity.ok(PageDTO.fromPage(pageResult));
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
