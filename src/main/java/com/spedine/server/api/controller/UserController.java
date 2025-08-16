package com.spedine.server.api.controller;

import com.spedine.server.api.dto.CreateUserDTO;
import com.spedine.server.domain.entity.Student;
import com.spedine.server.domain.entity.User;
import com.spedine.server.domain.service.StudentBeltService;
import com.spedine.server.domain.service.StudentService;
import com.spedine.server.domain.service.UserService;
import com.spedine.server.dto.UserDashboardInfoDTO;
import com.spedine.server.dto.UserListingInfoDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final StudentBeltService studentBeltService;
    private final StudentService studentService;

    public UserController(UserService userService, StudentBeltService studentBeltService, StudentService studentService) {
        this.userService = userService;
        this.studentBeltService = studentBeltService;
        this.studentService = studentService;
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('MASTER')")
    @SecurityRequirement(name = "bearer-key")
    @Transactional
    public ResponseEntity<Void> registerUser(@RequestBody @Valid CreateUserDTO dto) {
        Student student = studentService.registerStudentByDto(dto.student(), null, null);
        User user = userService.registerUser(dto.login(), dto.role(), student);
        studentBeltService.registerMultipleBeltsForStudent(user.getStudent(), dto.belts());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/info")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<UserDashboardInfoDTO> getUserInfo(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return ResponseEntity.ok(new UserDashboardInfoDTO(user.getStudent().getName(), user.getUsername(), user.getRole()));
    }

    @GetMapping("/list/all")
    public ResponseEntity<List<UserListingInfoDTO>> listAllUsers() {
        return ResponseEntity.ok(userService.listAll());
    }
}
