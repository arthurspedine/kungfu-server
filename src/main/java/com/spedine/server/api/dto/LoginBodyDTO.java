package com.spedine.server.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginBodyDTO(
        @Email
        @NotNull
        String email,
        @NotNull
        String password
) {
}
