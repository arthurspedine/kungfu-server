package com.spedine.server.domain.validations.training_center.update;

import com.spedine.server.api.dto.EditTrainingCenterDTO;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ValidateClosingDate implements UpdateTrainingCenterHandler{
    @Override
    public void validate(EditTrainingCenterDTO dto) {
        if (dto.closingDate() != null) {
            if (dto.closingDate().isBefore(dto.openingDate()) || dto.closingDate().isEqual(dto.openingDate())) {
                throw new ValidationException("A data de fechamento deve ser posterior a data de inauguração.");
            }
        }
    }
}
