package com.spedine.server.domain.service;

import com.spedine.server.api.dto.CreateTrainingCenterDTO;
import com.spedine.server.api.dto.StudentInformationDTO;
import com.spedine.server.domain.entity.Student;
import com.spedine.server.domain.entity.TrainingCenter;
import com.spedine.server.domain.entity.User;
import com.spedine.server.domain.repository.TrainingCenterRepository;
import com.spedine.server.dto.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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
        if (dto.openingDate().isAfter(LocalDate.now()))
            throw new ValidationException("A data de inauguração deve ser antes de hoje.");
        User user = userService.findUserById(dto.teacherId());
        if (!user.hasTeacherRole())
            throw new RuntimeException();
        TrainingCenter trainingCenter = new TrainingCenter();
        trainingCenter.setTeacher(user);
        trainingCenter.setName(dto.name());
        trainingCenter.setNumber(dto.number());
        trainingCenter.setAdditionalAddress(dto.additionalAddress());
        trainingCenter.setStreet(dto.street());
        trainingCenter.setCity(dto.city());
        trainingCenter.setState(dto.state());
        trainingCenter.setZipCode(dto.zipCode().replace("-", ""));
        trainingCenter.setOpeningDate(dto.openingDate());
        repository.save(trainingCenter);
    }

    public TrainingCenter findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Nucleo nao encontrado."));
    }

    public List<TrainingCenterInfoDTO> findAllTrainingCenterDTO() {
        List<TrainingCenter> trainingCenters = repository.findAll();
        return trainingCenters.stream().map(this::mapperToDTO).toList();
    }

    public List<TrainingCenter> findAllByUser(User user) {
        return repository.findAllByTeacher(user);
    }

    public TrainingCenterInfoDTO getInfoById(User user, UUID id) {
        TrainingCenter trainingCenter = findById(id);
        if (!trainingCenter.getTeacher().equals(user) && !user.isMaster()) {
            throw new RuntimeException("Você não pode acessar este núcleo.");
        }
        return mapperToDTO(trainingCenter);
    }

    public TrainingCenterDetailsDTO getDetailsById(User user, UUID id) {
        TrainingCenter trainingCenter = findById(id);
        if (!trainingCenter.getTeacher().equals(user) && !user.isMaster()) {
            throw new RuntimeException("Você não pode acessar este núcleo.");
        }
        return new TrainingCenterDetailsDTO(
                mapperToDTO(trainingCenter), trainingCenter.getStudents().stream().map(this::mapperToStudentDTO).toList()
        );
    }

    public List<TrainingCenterSimpleInfoDTO> findAllInfo(User user) {
        List<TrainingCenter> trainingCenters;
        if (user.isMaster()) {
            trainingCenters = repository.findAll();
        } else {
            trainingCenters = findAllByUser(user);
        }
        return trainingCenters.stream().map(trainingCenter -> new TrainingCenterSimpleInfoDTO(
                trainingCenter.getId(), trainingCenter.getName(), trainingCenter.getTeacher().getName()
        )).toList();
    }

    private TrainingCenterInfoDTO mapperToDTO(TrainingCenter trainingCenter) {
        User teacher = trainingCenter.getTeacher();
        return new TrainingCenterInfoDTO(trainingCenter.getId(),
                new TeacherDTO(teacher.getId(), teacher.getName(), teacher.getCurrentBelt().getName().getDescription(), teacher.getSex().getDescription()),
                trainingCenter.getStudents().size(), trainingCenter.getName(),
                trainingCenter.getStreet(), trainingCenter.getNumber(), trainingCenter.getAdditionalAddress(),
                trainingCenter.getCity(), trainingCenter.getState(), trainingCenter.getOpeningDate().toString(),
                trainingCenter.getClosingDate() != null ? trainingCenter.getClosingDate().toString() : null);
    }

    private TrainingCenterStudentDTO mapperToStudentDTO(Student student) {
        return new TrainingCenterStudentDTO(
                student.getId(),
                new StudentInformationDTO(student.getName(), student.getBirthDate().toString(), student.getSex()),
                student.getCurrentBelt().getName().getDescription());
    }
}
