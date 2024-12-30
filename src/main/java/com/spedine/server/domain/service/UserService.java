package com.spedine.server.domain.service;

import com.spedine.server.api.dto.CreateUserDTO;
import com.spedine.server.domain.entity.Belt;
import com.spedine.server.domain.entity.User;
import com.spedine.server.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final StudentBeltService studentBeltService;

    private final BeltService beltService;

    public UserService(UserRepository repository, StudentBeltService studentBeltService, BeltService beltService) {
        this.repository = repository;
        this.studentBeltService = studentBeltService;
        this.beltService = beltService;
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
        repository.save(user);

        // Belt setup
        Belt belt = beltService.findBeltByEnumType(dto.belt().type());
        studentBeltService.registerBeltForStudent(user, belt, dto.belt().achievedDate());

        return user;
    }

    public User findUserById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario nao encontrado."));
    }
}
