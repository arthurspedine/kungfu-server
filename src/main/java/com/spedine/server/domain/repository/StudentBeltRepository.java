package com.spedine.server.domain.repository;

import com.spedine.server.domain.entity.StudentBelt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentBeltRepository extends JpaRepository<StudentBelt, UUID> {
}
