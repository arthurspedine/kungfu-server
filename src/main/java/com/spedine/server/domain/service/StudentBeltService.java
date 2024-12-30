package com.spedine.server.domain.service;

import com.spedine.server.domain.entity.Belt;
import com.spedine.server.domain.entity.Student;
import com.spedine.server.domain.entity.StudentBelt;
import com.spedine.server.domain.repository.StudentBeltRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StudentBeltService {

    private final StudentBeltRepository repository;

    public StudentBeltService(StudentBeltRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void registerBeltForStudent(Student student, Belt belt, String achievedDate)  {
        LocalDate parsedAchivedDate = LocalDate.parse(achievedDate);
        StudentBelt studentBelt = new StudentBelt(student, belt, parsedAchivedDate);
        repository.save(studentBelt);
    }

}
