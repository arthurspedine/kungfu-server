package com.spedine.server.domain.service;

import com.spedine.server.api.dto.FormStudentDTO;
import com.spedine.server.api.dto.StudentInformationDTO;
import com.spedine.server.domain.entity.Student;
import com.spedine.server.domain.entity.StudentBelt;
import com.spedine.server.domain.entity.TrainingCenter;
import com.spedine.server.domain.entity.User;
import com.spedine.server.domain.repository.StudentRepository;
import com.spedine.server.dto.BeltInfoDTO;
import com.spedine.server.dto.StudentDetailsDTO;
import com.spedine.server.dto.StudentInfoDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentRepository repository;

    private final TrainingCenterService trainingCenterService;

    public StudentService(StudentRepository repository, TrainingCenterService trainingCenterService) {
        this.repository = repository;
        this.trainingCenterService = trainingCenterService;
    }

    @Transactional
    public Student registerStudentByDto(FormStudentDTO dto, User teacher) {
        Student student = new Student();
        TrainingCenter trainingCenter = trainingCenterService.findById(dto.trainingCenterId());
        validateTeacherAccess(teacher, trainingCenter);
        setStudentVariables(student, dto.student(), trainingCenter);
        return repository.save(student);
    }

    public Student findStudentById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Estudante nao encontrado."));
    }

    public StudentDetailsDTO getStudentDetails(UUID id) {
        Student student = findStudentById(id);
        StudentInformationDTO studentInformationDTO = new StudentInformationDTO(student.getName(), student.getBirthDate().toString(), student.getSex());
        List<StudentBelt> belts = student.getBelts();
        List<BeltInfoDTO> beltsDTO = belts.stream().map(b -> new BeltInfoDTO(b.getBelt().getName().getDescription(), b.getAchievedDate().toString())).toList();
        return new StudentDetailsDTO(student.getId(), studentInformationDTO, beltsDTO, student.getTrainingCenter().getId());
    }

    public List<StudentInfoDTO> listAll(User user) {
        return repository.listAllStudents(user.isMaster(), user.getId());
    }

    @Transactional
    public Student updateStudentByDto(UUID id, FormStudentDTO dto, User teacher) {
        Student student = findStudentById(id);
        TrainingCenter trainingCenter = trainingCenterService.findById(dto.trainingCenterId());
        validateTeacherAccess(teacher, trainingCenter);
        setStudentVariables(student, dto.student(), trainingCenter);
        return repository.save(student);
    }

    private void setStudentVariables(Student student, StudentInformationDTO dto, TrainingCenter trainingCenter) {
        student.setName(dto.name());
        student.setBirthDate(LocalDate.parse(dto.birthDate()));
        student.setSex(dto.sex());
        student.setTrainingCenter(trainingCenter);
    }

    private void validateTeacherAccess(User teacher, TrainingCenter trainingCenter) {
        if (!teacher.isMaster() || !teacher.isAdmin()) {
            if (!trainingCenter.getTeacher().equals(teacher)) {
                throw new ValidationException("Adicione o aluno somente a núcleos que você é professor.");
            }
        }
    }
}
