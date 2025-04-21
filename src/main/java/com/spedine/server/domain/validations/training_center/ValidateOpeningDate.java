package com.spedine.server.domain.validations.training_center;

import com.spedine.server.api.dto.CreateTrainingCenterDTO;
import com.spedine.server.api.dto.EditTrainingCenterDTO;
import com.spedine.server.domain.validations.training_center.create.CreateTrainingCenterHandler;
import com.spedine.server.domain.validations.training_center.update.UpdateTrainingCenterHandler;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ValidateOpeningDate implements UpdateTrainingCenterHandler, CreateTrainingCenterHandler {

    private final String validationMessage = "A data de inauguração deve ser antes de hoje.";

    @Override
    public void validate(EditTrainingCenterDTO dto) {
        if (dto.openingDate().isAfter(LocalDate.now()))
            throw new ValidationException(validationMessage);
    }

    @Override
    public void validate(CreateTrainingCenterDTO dto) {
        if (dto.openingDate().isAfter(LocalDate.now()))
            throw new ValidationException(validationMessage);
    }
}
