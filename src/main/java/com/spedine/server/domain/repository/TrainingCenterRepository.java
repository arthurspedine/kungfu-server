package com.spedine.server.domain.repository;

import com.spedine.server.domain.entity.TrainingCenter;
import com.spedine.server.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TrainingCenterRepository extends JpaRepository<TrainingCenter, UUID> {
    List<TrainingCenter> findAllByTeacher(User teacher);

    @Query("select t from TrainingCenter t order by t.name asc")
    Page<TrainingCenter> findAllAsc(Pageable pageable);
}
