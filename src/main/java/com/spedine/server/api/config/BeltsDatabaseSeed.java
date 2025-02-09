package com.spedine.server.api.config;

import com.spedine.server.domain.entity.Belt;
import com.spedine.server.domain.entity.EBelt;
import com.spedine.server.domain.repository.BeltRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BeltsDatabaseSeed {

    private static final Logger log = LoggerFactory.getLogger(BeltsDatabaseSeed.class);
    private final BeltRepository beltRepository;

    public BeltsDatabaseSeed(BeltRepository beltRepository) {
        this.beltRepository = beltRepository;
    }

    @PostConstruct
    public void initializeBelts() {
        for (EBelt eBelt : EBelt.values()) {
            if (!beltRepository.existsByName(eBelt)) {
                beltRepository.save(new Belt(eBelt));
                log.info("Added belt: {}", eBelt);
            }
        }
    }
}
