package com.spedine.server.api.controller;

import com.spedine.server.api.dto.CreateTrainingCenterDTO;
import com.spedine.server.domain.entity.TrainingCenter;
import com.spedine.server.domain.entity.User;
import com.spedine.server.domain.service.TrainingCenterService;
import com.spedine.server.dto.TrainingCenterDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('TEACHER', 'MASTER')")
    public ResponseEntity<?> listAll(Authentication auth) {
        User user = (User) auth.getPrincipal();
        List<TrainingCenterDTO> centers = service.findAllTrainingCenterDTOByUser(user);
        return ResponseEntity.ok(centers);
    }
}
