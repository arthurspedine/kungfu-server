package com.spedine.server.domain.validations.training_center.create;

import com.spedine.server.api.dto.CreateTrainingCenterDTO;

public interface CreateTrainingCenterHandler {
    void validate(CreateTrainingCenterDTO dto);
}
