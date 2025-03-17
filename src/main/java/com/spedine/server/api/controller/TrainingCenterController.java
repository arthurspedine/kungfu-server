package com.spedine.server.api.controller;

import com.spedine.server.api.dto.CreateTrainingCenterDTO;
import com.spedine.server.domain.entity.User;
import com.spedine.server.domain.service.TrainingCenterService;
import com.spedine.server.dto.TrainingCenterInfoDTO;
import com.spedine.server.dto.TrainingCenterDetailsDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<Void> addTrainingCenter(@RequestBody @Valid CreateTrainingCenterDTO dto) {
        service.registerTrainingCenter(dto);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('TEACHER', 'MASTER')")
    public ResponseEntity<List<TrainingCenterInfoDTO>> listAll() {
        List<TrainingCenterInfoDTO> centers = service.findAllTrainingCenterDTO();
        return ResponseEntity.ok(centers);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<TrainingCenterInfoDTO> getInfoFromTrainingCenterId(@PathVariable UUID id, Authentication auth) {
        User user = (User) auth.getPrincipal();
        return ResponseEntity.ok(service.getInfoById(user ,id));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<TrainingCenterDetailsDTO> getDetailsFromTrainingCenterId(@PathVariable UUID id, Authentication auth) {
        User user = (User) auth.getPrincipal();
        TrainingCenterDetailsDTO details = service.getDetailsById(user, id);
        return ResponseEntity.ok(details);
    }
}
