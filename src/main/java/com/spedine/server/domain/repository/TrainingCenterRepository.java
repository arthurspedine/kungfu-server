package com.spedine.server.domain.repository;

import com.spedine.server.domain.entity.TrainingCenter;
import com.spedine.server.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TrainingCenterRepository extends JpaRepository<TrainingCenter, UUID> {
    List<TrainingCenter> findAllByTeacher(User teacher);
}
