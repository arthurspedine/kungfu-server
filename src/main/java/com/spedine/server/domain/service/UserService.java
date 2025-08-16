package com.spedine.server.domain.service;

import com.spedine.server.api.dto.LoginBodyDTO;
import com.spedine.server.domain.entity.ERole;
import com.spedine.server.domain.entity.Student;
import com.spedine.server.domain.entity.User;
import com.spedine.server.domain.repository.UserRepository;
import com.spedine.server.dto.UserListingInfoDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository repository) {
        this.repository = repository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public User registerUser(LoginBodyDTO dto, ERole role, Student student) {
        User user = new User();

        user.setStudent(student);
        // User setup
        if (dto.email() == null || dto.password() == null) {
            throw new IllegalArgumentException("Login information is required.");
        }
        String encryptedPassword = passwordEncoder.encode(dto.password());
        user.setEmail(dto.email());
        user.setPassword(encryptedPassword);
        user.setRole(role);

        // Save to get UUID
        return repository.save(user);
    }

    public User findUserById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario nao encontrado."));
    }

    public List<UserListingInfoDTO> listAll() {
        return repository.listAllUsers();
    }
}
