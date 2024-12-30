package com.spedine.server.domain.service;

import com.spedine.server.api.dto.CreateTrainingCenterDTO;
import com.spedine.server.domain.entity.TrainingCenter;
import com.spedine.server.domain.entity.User;
import com.spedine.server.domain.repository.TrainingCenterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TrainingCenterService {

    private final TrainingCenterRepository repository;

    private final UserService userService;

    public TrainingCenterService(TrainingCenterRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public void registerTrainingCenter(CreateTrainingCenterDTO dto) {
        User user = userService.findUserById(dto.teacherId());
        if (!user.hasTeacherRole())
            throw new RuntimeException();
        TrainingCenter trainingCenter = new TrainingCenter();
        trainingCenter.setTeacher(user);
        trainingCenter.setName(dto.name());
        trainingCenter.setNumber(dto.number());
        trainingCenter.setStreet(dto.street());
        trainingCenter.setCity(dto.city());
        trainingCenter.setState(dto.state());
        trainingCenter.setZipCode(dto.zipCode());
        repository.save(trainingCenter);
    }

    public TrainingCenter findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Nucleo nao encontrado."));
    }
}
