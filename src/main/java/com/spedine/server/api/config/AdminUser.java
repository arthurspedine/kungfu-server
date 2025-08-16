package com.spedine.server.api.config;

import com.spedine.server.api.dto.LoginBodyDTO;
import com.spedine.server.domain.entity.ERole;
import com.spedine.server.domain.repository.UserRepository;
import com.spedine.server.domain.service.StudentService;
import com.spedine.server.domain.service.UserService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AdminUser {

    private static final Logger log = LoggerFactory.getLogger(AdminUser.class);
    private final UserService userService;
    private final UserRepository userRepository;
    private final StudentService studentService;
    @Value("${api.security.admin.password}")
    private String password;

    public AdminUser(UserService userService, UserRepository userRepository, StudentService studentService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.studentService = studentService;
    }

    @PostConstruct
    private void createAdminUser() {
        String email = "admin@admin.com";
        UserDetails userDetails = userRepository.findByEmail(email);
        if (userDetails == null) {
            userService.registerUser(new LoginBodyDTO(email, password), ERole.ADMIN, null);
            log.info("Created Admin User!");
        }
    }
}
