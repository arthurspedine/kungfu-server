package com.spedine.server.api.controller;

import com.spedine.server.api.dto.UserInfoDTO;
import com.spedine.server.domain.entity.User;
import com.spedine.server.domain.service.StudentBeltService;
import com.spedine.server.domain.service.UserService;
import com.spedine.server.api.dto.CreateUserDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final StudentBeltService studentBeltService;

    public UserController(UserService userService, StudentBeltService studentBeltService) {
        this.userService = userService;
        this.studentBeltService = studentBeltService;
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('MASTER')")
    @Transactional
    public ResponseEntity<?> registerUser(@RequestBody @Valid CreateUserDTO dto) {
        User user = userService.registerUser(dto);
        studentBeltService.registerBeltForStudent(user, dto.belt());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoDTO> getUserInfo(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return ResponseEntity.ok(new UserInfoDTO(user.getName(), user.getUsername()));
    }
}
