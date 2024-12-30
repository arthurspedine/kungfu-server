package com.spedine.server.domain.repository;

import com.spedine.server.domain.entity.Belt;
import com.spedine.server.domain.entity.EBelt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeltRepository extends JpaRepository<Belt, UUID> {

    boolean existsByName(EBelt EBelt);

    Belt findByName(EBelt belt);
}
