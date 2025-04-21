package com.spedine.server.domain.service;

import com.spedine.server.api.dto.CreateTrainingCenterDTO;
import com.spedine.server.api.dto.EditTrainingCenterDTO;
import com.spedine.server.api.dto.StudentInformationDTO;
import com.spedine.server.domain.entity.Student;
import com.spedine.server.domain.entity.TrainingCenter;
import com.spedine.server.domain.entity.User;
import com.spedine.server.domain.repository.TrainingCenterRepository;
import com.spedine.server.domain.validations.training_center.create.CreateTrainingCenterHandler;
import com.spedine.server.domain.validations.training_center.update.UpdateTrainingCenterHandler;
import com.spedine.server.dto.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TrainingCenterService {

    private final TrainingCenterRepository repository;

    private final UserService userService;

    @Autowired
    private List<CreateTrainingCenterHandler> createValidations;

    @Autowired
    private List<UpdateTrainingCenterHandler> updateValidations;

    public TrainingCenterService(TrainingCenterRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public void registerTrainingCenter(CreateTrainingCenterDTO dto) {
        createValidations.forEach(validation -> validation.validate(dto));
        User user = userService.findUserById(dto.teacherId());
        if (!user.hasTeacherRole())
            throw new ValidationException("O professor deve ter permissão para ser o professor docente do núcleo.");
        TrainingCenter trainingCenter = new TrainingCenter();
        trainingCenter.setTeacher(user);
        setDefaultFields(trainingCenter, dto.name(), dto.number(), dto.additionalAddress(), dto.street(), dto.city(), dto.state(), dto.zipCode(), dto.openingDate());
        repository.save(trainingCenter);
    }

    public TrainingCenter findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Nucleo nao encontrado."));
    }

    public List<TrainingCenterInfoDTO> findAllTrainingCenterDTO() {
        List<TrainingCenter> trainingCenters = repository.findAllAsc();
        return trainingCenters.stream().map(this::mapperToDTO).toList();
    }

    public List<TrainingCenter> findAllByUser(User user) {
        return repository.findAllByTeacher(user);
    }

    public TrainingCenterInfoDTO getInfoById(User user, UUID id) {
        TrainingCenter trainingCenter = findById(id);
        if (!trainingCenter.getTeacher().equals(user) && !user.isMaster()) {
            throw new ValidationException("Você não pode acessar este núcleo.");
        }
        return mapperToDTO(trainingCenter);
    }

    public TrainingCenterDetailsDTO getDetailsById(User user, UUID id) {
        TrainingCenter trainingCenter = findById(id);
        if (!trainingCenter.getTeacher().equals(user) && !user.isMaster()) {
            throw new ValidationException("Você não pode acessar este núcleo.");
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

    public void updateTrainingCenter(UUID id, EditTrainingCenterDTO dto, User user) {
        TrainingCenter trainingCenter = findById(id);
        User currentTeacher = trainingCenter.getTeacher();
        if (!currentTeacher.equals(user) && !user.isMaster()) {
            throw new RuntimeException("Você não pode atualizar este núcleo.");
        }
        updateValidations.forEach(validation -> validation.validate(dto));
        User newTeacher = dto.teacherId() == user.getId() ? user : userService.findUserById(dto.teacherId());
        if (!currentTeacher.equals(newTeacher)) {
            if (!newTeacher.hasTeacherRole())
                throw new ValidationException("O professor deve ter permissão para ser o professor docente do núcleo.");
            trainingCenter.setTeacher(newTeacher);
        }
        setDefaultFields(trainingCenter, dto.name(), dto.number(), dto.additionalAddress(), dto.street(), dto.city(), dto.state(), dto.zipCode(), dto.openingDate());
        trainingCenter.setClosingDate(dto.closingDate());
        repository.save(trainingCenter);
    }

    private void setDefaultFields(TrainingCenter trainingCenter, String name, int number, String s, String street, String city, String state, String s2, LocalDate localDate) {
        trainingCenter.setName(name);
        trainingCenter.setNumber(number);
        trainingCenter.setAdditionalAddress(s);
        trainingCenter.setStreet(street);
        trainingCenter.setCity(city);
        trainingCenter.setState(state);
        trainingCenter.setZipCode(s2.replace("-", ""));
        trainingCenter.setOpeningDate(localDate);
    }

    private TrainingCenterInfoDTO mapperToDTO(TrainingCenter trainingCenter) {
        User teacher = trainingCenter.getTeacher();
        return new TrainingCenterInfoDTO(trainingCenter.getId(),
                new TeacherDTO(teacher.getId(), teacher.getName(), teacher.getCurrentBelt().getName().getDescription(), teacher.getSex().getDescription()),
                trainingCenter.getStudents().size(), trainingCenter.getName(),
                trainingCenter.getStreet(), trainingCenter.getNumber(), trainingCenter.getAdditionalAddress(),
                trainingCenter.getCity(), trainingCenter.getState(), trainingCenter.getZipCode(),
                trainingCenter.getOpeningDate().toString(), trainingCenter.getClosingDate() != null ? trainingCenter.getClosingDate().toString() : null);
    }

    private TrainingCenterStudentDTO mapperToStudentDTO(Student student) {
        return new TrainingCenterStudentDTO(
                student.getId(),
                new StudentInformationDTO(student.getName(), student.getBirthDate().toString(), student.getSex()),
                student.getCurrentBelt().getName().getDescription());
    }
}
