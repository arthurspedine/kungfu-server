package com.spedine.server.api.controller;

import com.spedine.server.api.dto.CreateTrainingCenterDTO;
import com.spedine.server.domain.service.TrainingCenterService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/training-center")
public class TrainingCenterController {

    private final TrainingCenterService service;

    public TrainingCenterController(TrainingCenterService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @Transactional
    @PreAuthorize("hasAnyRole('TEACHER', 'MASTER')")
    public ResponseEntity<?> addTrainingCenter(@RequestBody @Valid CreateTrainingCenterDTO dto) {
        service.registerTrainingCenter(dto);
        return ResponseEntity.status(201).build();
    }
}
