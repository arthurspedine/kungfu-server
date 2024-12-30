package com.spedine.server.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

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
        @NotNull
        String city,
        @NotNull
        String state,
        @NotNull
        @Pattern(regexp = "^\\d{8}$", message = "O código postal deve ser apenas números e ter 8 digitos.")
        String zipCode
) {
}
