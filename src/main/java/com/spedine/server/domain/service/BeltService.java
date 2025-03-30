package com.spedine.server.domain.service;

import com.spedine.server.domain.entity.Belt;
import com.spedine.server.domain.entity.EBelt;
import com.spedine.server.domain.repository.BeltRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BeltService {

    private final BeltRepository repository;

    public BeltService(BeltRepository repository) {
        this.repository = repository;
    }

    public Belt findBeltByEnumType(EBelt belt) {
        Belt foundedBelt = repository.findByName(belt);
        if (foundedBelt == null)
            throw new EntityNotFoundException("Belt not found!");
        return foundedBelt;
    }

    public List<Map<EBelt, String>> listAllBets() {
        List<Belt> belts = repository.findAll();
        return belts.stream()
                .map(belt -> {
                            Map<EBelt, String> betMap = new HashMap<>();
                            betMap.put(belt.getName(), belt.getName().getDescription());
                            return betMap;
                        }
                ).collect(Collectors.toList());
    }
}
