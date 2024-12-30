package com.spedine.server.domain.repository;

import com.spedine.server.domain.entity.TrainingCenter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TrainingCenterRepository extends JpaRepository<TrainingCenter, UUID> {
}
