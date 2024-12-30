package com.spedine.server.domain.service;

import com.spedine.server.domain.entity.Belt;
import com.spedine.server.domain.entity.EBelt;
import com.spedine.server.domain.repository.BeltRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

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
}
