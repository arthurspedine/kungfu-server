package com.spedine.server.api.config;

import com.spedine.server.api.dto.CreateUserDTO;
import com.spedine.server.api.dto.LoginBodyDTO;
import com.spedine.server.api.dto.StudentInformationDTO;
import com.spedine.server.domain.entity.ERole;
import com.spedine.server.domain.entity.ESex;
import com.spedine.server.domain.repository.UserRepository;
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
    @Value("${api.security.admin.password}")
    private String password;

    public AdminUser(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void createAdminUser() {
        String email = "admin@admin.com";
        UserDetails userDetails = userRepository.findByEmail(email);
        if (userDetails == null) {
            CreateUserDTO dto = new CreateUserDTO(
                    new StudentInformationDTO("Admin User", "2000-01-01", ESex.M),
                    ERole.MASTER, null, new LoginBodyDTO(email, password));
            userService.registerUser(dto);
            log.info("Created Admin User!");
        }
    }
}
