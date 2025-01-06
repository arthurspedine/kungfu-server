package com.spedine.server.api.config;

import com.spedine.server.domain.entity.Belt;
import com.spedine.server.domain.entity.EBelt;
import com.spedine.server.domain.repository.BeltRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class BeltsDatabaseSeed {

    private final BeltRepository beltRepository;

    public BeltsDatabaseSeed(BeltRepository beltRepository) {
        this.beltRepository = beltRepository;
    }

    @PostConstruct
    public void initializeBelts() {
        for (EBelt eBelt : EBelt.values()) {
            if (!beltRepository.existsByName(eBelt)) {
                beltRepository.save(new Belt(eBelt));
                System.out.println("Added name: " + eBelt);
            }
        }
    }
}
