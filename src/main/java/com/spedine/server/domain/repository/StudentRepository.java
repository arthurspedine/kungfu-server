package com.spedine.server.domain.repository;

import com.spedine.server.domain.entity.Student;
import com.spedine.server.dto.StudentInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {
    @Query(value = "SELECT " +
            "s.id, " +
            "s.name, " +
            "TO_CHAR(s.birth_date, 'DD/MM/YYYY') AS birth_date, " +
            "EXTRACT(YEAR FROM AGE(CURRENT_DATE, s.birth_date))::INTEGER AS age, " +
            "s.sex AS sex,  " +
            "b.name AS current_belt, " +
            "EXTRACT(MONTH FROM AGE(CURRENT_DATE, latest_belt.achieved_date))::INTEGER + " +
            "(EXTRACT(YEAR FROM AGE(CURRENT_DATE, latest_belt.achieved_date))::INTEGER * 12) AS belt_age_months " +
            "FROM students s " +
            "LEFT JOIN LATERAL ( " +
            "    SELECT sb.belt_id, sb.achieved_date " +
            "    FROM student_belts sb " +
            "    WHERE sb.student_id = s.id " +
            "    ORDER BY sb.achieved_date DESC " +
            "    LIMIT 1 " +
            ") latest_belt ON true " +
            "LEFT JOIN belts b ON b.id = latest_belt.belt_id " +
            "LEFT JOIN users u ON u.id = s.id " +
            "LEFT JOIN training_centers tc ON tc.id = s.training_center_id " +
            "WHERE s.active = true " +
            "AND (:isMaster = true OR u IS NULL OR u.role != 'ADMIN') " +
            "AND (:isMaster = true OR tc.teacher_id = :userId) " +
            "AND s.training_center_id IS NOT NULL " +
            "ORDER BY s.name",
            nativeQuery = true)
    List<StudentInfoDTO> listAllStudents(@Param("isMaster") boolean isMaster, UUID userId);

}
