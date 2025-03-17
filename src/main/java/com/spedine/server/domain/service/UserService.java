package com.spedine.server.domain.service;

import com.spedine.server.api.dto.CreateUserDTO;
import com.spedine.server.dto.UserListingInfoDTO;
import com.spedine.server.domain.entity.User;
import com.spedine.server.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public User registerUser(CreateUserDTO dto) {
        User user = new User();
        // Student setup
        user.setName(dto.student().name());
        user.setBirthDate(LocalDate.parse(dto.student().birthDate()));
        user.setSex(dto.student().sex());

        // User setup
        String encryptedPassword = passwordEncoder.encode(dto.login().password());
        user.setEmail(dto.login().email());
        user.setPassword(encryptedPassword);
        user.setRole(dto.role());

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
