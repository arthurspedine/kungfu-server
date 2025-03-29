package com.spedine.server.api.controller;

import com.spedine.server.domain.entity.EBelt;
import com.spedine.server.domain.service.BeltService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/belt")
public class BeltController {

    private final BeltService service;

    public BeltController(BeltService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Map<EBelt, String>>> listAllBelts() {
        return ResponseEntity.ok(service.listAllBets());
    }
}
