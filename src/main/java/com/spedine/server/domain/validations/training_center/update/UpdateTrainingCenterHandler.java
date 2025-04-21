package com.spedine.server.domain.validations.training_center.update;

import com.spedine.server.api.dto.EditTrainingCenterDTO;

public interface UpdateTrainingCenterHandler {
    void validate(EditTrainingCenterDTO dto);
}
