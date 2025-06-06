package com.spedine.server.api.controller;

import com.spedine.server.api.dto.CreateTrainingCenterDTO;
import com.spedine.server.api.dto.EditTrainingCenterDTO;
import com.spedine.server.api.dto.PageDTO;
import com.spedine.server.domain.entity.User;
import com.spedine.server.domain.service.TrainingCenterService;
import com.spedine.server.dto.TrainingCenterDetailsDTO;
import com.spedine.server.dto.TrainingCenterInfoDTO;
import com.spedine.server.dto.TrainingCenterSimpleInfoDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/training-center")
@SecurityRequirement(name = "bearer-key")
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
    public ResponseEntity<PageDTO<TrainingCenterInfoDTO>> listAll(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TrainingCenterInfoDTO> pageResult = service.findAllTrainingCenterDTO(pageable);
        return ResponseEntity.ok(PageDTO.fromPage(pageResult));
    }

    @GetMapping("/all/info")
    @PreAuthorize("hasAnyRole('TEACHER', 'MASTER')")
    public ResponseEntity<List<TrainingCenterSimpleInfoDTO>> listAllInfo() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(service.findAllInfo(user));
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<TrainingCenterInfoDTO> getInfoFromTrainingCenterId(@PathVariable UUID id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(service.getInfoById(user, id));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<TrainingCenterDetailsDTO> getDetailsFromTrainingCenterId(@PathVariable UUID id, Authentication auth) {
        User user = (User) auth.getPrincipal();
        TrainingCenterDetailsDTO details = service.getDetailsById(user, id);
        return ResponseEntity.ok(details);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Void> editTrainingCenter(
            @PathVariable UUID id,
            @RequestBody EditTrainingCenterDTO dto,
            Authentication auth
    ) {
        User user = (User) auth.getPrincipal();
        service.updateTrainingCenter(id, dto, user);
        return ResponseEntity.ok().build();
    }
}
