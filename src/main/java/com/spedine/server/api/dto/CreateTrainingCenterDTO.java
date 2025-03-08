package com.spedine.server.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.UUID;

public record CreateTrainingCenterDTO(
        @NotNull
        UUID teacherId,
        @NotNull
        String name,
        @NotNull
        String street,
        @NotNull
        int number,
        String additionalAddress,
        @NotNull
        String city,
        @NotNull
        String state,
        @NotNull
        @Pattern(regexp = "^\\d{5}-\\d{3}$", message = "O código postal deve ter o formato 00000-000.")
        String zipCode,
        @NotNull
        @Past(message = "A data de inauguração deve ser antes de hoje.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate openingDate
) {
}
