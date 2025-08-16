package com.spedine.server.dto;

import com.spedine.server.domain.entity.ERole;

import java.util.UUID;

public record UserDashboardInfoDTO(
        UUID id,
        String name,
        String email,
        ERole role
) {
}
