package com.spedine.server.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddBeltToStudent(
        @NotNull
        UUID studentId,
        @NotNull
        BeltDataDTO belt
) {
}
