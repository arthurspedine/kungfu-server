package com.spedine.server.domain.service;

import com.spedine.server.api.dto.BeltDataDTO;
import com.spedine.server.domain.entity.EBelt;
import com.spedine.server.domain.validations.student_belt.StudentBeltValidationHandler;
import com.spedine.server.domain.entity.Belt;
import com.spedine.server.domain.entity.Student;
import com.spedine.server.domain.entity.StudentBelt;
import com.spedine.server.domain.repository.StudentBeltRepository;
import com.spedine.server.dto.BeltInfoDTO;
import com.spedine.server.dto.StudentBeltDTO;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentBeltService {

    private final Logger logger = LoggerFactory.getLogger(StudentBeltService.class);

    private final StudentBeltRepository repository;

    private final BeltService beltService;

    @Autowired
    private List<StudentBeltValidationHandler> validations;

    public StudentBeltService(StudentBeltRepository repository, BeltService beltService) {
        this.repository = repository;
        this.beltService = beltService;
    }

    @Transactional
    public StudentBelt registerBeltForStudent(Student student, BeltDataDTO dto)  {
        logger.info("Registrando faixa {} para o estudante ID: {}", dto.type(), student.getId());

        Belt belt = beltService.findBeltByEnumType(dto.type());
        LocalDate parsedAchievedDate = LocalDate.parse(dto.achievedDate());
        StudentBelt studentBelt = new StudentBelt(student, belt, parsedAchievedDate);

        validations.forEach(validation -> validation.validate(student, studentBelt));

        logger.info("Salvando studentBelt para o estudante ID: {}", student.getId());
        return repository.save(studentBelt);
    }

    @Transactional
    public void registerMultipleBeltsForStudent(Student student, List<BeltDataDTO> dto)  {
        logger.info("Registrando várias faixas para o estudante ID: {}, quantidade de faixas: {}", student.getId(), dto.size());
        saveBeltsDtoToStudent(student, dto);
    }

    public StudentBeltDTO mapperToDTO(StudentBelt studentBelt) {
        logger.debug("Mapeando StudentBelt com ID: {} para DTO", studentBelt.getId());
        return new StudentBeltDTO(
                studentBelt.getId(), studentBelt.getStudent().getId(),
                new BeltInfoDTO(studentBelt.getBelt().getName().getDescription(),
                        studentBelt.getAchievedDate().toString()));
    }

    @Transactional
    public void updateBeltsForStudent(Student student, List<BeltDataDTO> dto) {
        logger.info("Atualizando faixas para o estudante ID: {}, quantidade de faixas solicitadas: {}", student.getId(), dto.size());

        Set<EBelt> requestedBeltTypes = dto.stream()
                .map(BeltDataDTO::type)
                .collect(Collectors.toSet());

        List<StudentBelt> currentBelts = new ArrayList<>(student.getBelts());
        logger.debug("Quantidade de faixas atuais: {}", currentBelts.size());

        List<StudentBelt> beltsToRemove = currentBelts.stream()
                .filter(studentBelt -> !requestedBeltTypes.contains(studentBelt.getBelt().getName()))
                .toList();

        if (!beltsToRemove.isEmpty()) {
            logger.info("Removendo {} faixas do estudante ID: {}", beltsToRemove.size(), student.getId());
            student.getBelts().removeAll(beltsToRemove);
            repository.deleteAll(beltsToRemove);
        }

        Set<EBelt> existingBeltTypes = currentBelts.stream()
                .map(sb -> sb.getBelt().getName())
                .collect(Collectors.toSet());

        List<BeltDataDTO> beltsToAdd = dto.stream()
                .filter(belt -> !existingBeltTypes.contains(belt.type()))
                .toList();

        if (!beltsToAdd.isEmpty()) {
            logger.info("Adicionando {} novas faixas ao estudante ID: {}", beltsToAdd.size(), student.getId());
            saveBeltsDtoToStudent(student, beltsToAdd);
        }
    }

    private void saveBeltsDtoToStudent(Student student, List<BeltDataDTO> newBelts) {
        logger.debug("Salvando {} faixas no estudante ID: {}", newBelts.size(), student.getId());

        List<StudentBelt> beltsToSave = new ArrayList<>();
        for (BeltDataDTO beltDTO : newBelts) {
            logger.debug("Processando faixa: {} com data: {}", beltDTO.type(), beltDTO.achievedDate());
            Belt belt = beltService.findBeltByEnumType(beltDTO.type());
            LocalDate parsedAchievedDate = LocalDate.parse(beltDTO.achievedDate());
            StudentBelt studentBelt = new StudentBelt(student, belt, parsedAchievedDate);
            validations.forEach(validation -> validation.validate(student, studentBelt));
            student.getBelts().add(studentBelt);
            beltsToSave.add(studentBelt);
        }

        logger.info("Salvando {} faixas de estudante no repositório", beltsToSave.size());
        repository.saveAll(beltsToSave);
    }

}
