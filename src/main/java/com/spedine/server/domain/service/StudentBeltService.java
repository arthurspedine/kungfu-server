package com.spedine.server.domain.service;

import com.spedine.server.api.dto.BeltDataDTO;
import com.spedine.server.domain.entity.Belt;
import com.spedine.server.domain.entity.EBelt;
import com.spedine.server.domain.entity.Student;
import com.spedine.server.domain.entity.StudentBelt;
import com.spedine.server.domain.repository.StudentBeltRepository;
import com.spedine.server.domain.validations.student_belt.StudentBeltValidationHandler;
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
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
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
        logger.info("Atualizando faixas para o estudante ID: {}", student.getId());

        Map<EBelt, StudentBelt> currentBeltsMap = student.getBelts().stream()
                .collect(Collectors.toMap(sb -> sb.getBelt().getName(), Function.identity()));

        Map<EBelt, BeltDataDTO> newBeltsMap = dto.stream()
                .collect(Collectors.toMap(BeltDataDTO::type, Function.identity()));

        Set<StudentBelt> beltsToRemove = currentBeltsMap.entrySet().stream()
                .filter(entry -> !newBeltsMap.containsKey(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());

        if (!beltsToRemove.isEmpty()) {
            logger.info("Removendo {} faixas do estudante ID: {}", beltsToRemove.size(), student.getId());
            student.getBelts().removeAll(beltsToRemove);
            repository.deleteAll(beltsToRemove);
        }

       List<StudentBelt> beltsToSave = new ArrayList<>();

        for (BeltDataDTO beltDto : dto) {
            LocalDate newDate = LocalDate.parse(beltDto.achievedDate());
            StudentBelt existingBelt = currentBeltsMap.get(beltDto.type());

            if (existingBelt != null) {
                if (!existingBelt.getAchievedDate().isEqual(newDate)) {
                    existingBelt.setAchievedDate(newDate);
                    validations.forEach(validation -> validation.validate(student, existingBelt));
                    beltsToSave.add(existingBelt);
                }
            } else {
                Belt belt = beltService.findBeltByEnumType(beltDto.type());
                StudentBelt newStudentBelt = new StudentBelt(student, belt, newDate);
                validations.forEach(validation -> validation.validate(student, newStudentBelt));
                student.getBelts().add(newStudentBelt);
                beltsToSave.add(newStudentBelt);
            }
        }

        if (!beltsToSave.isEmpty()) {
            logger.info("Salvando {} faixas atualizadas/novas no repositório", beltsToSave.size());
            repository.saveAll(beltsToSave);
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
