package com.spedine.server.dto;

import com.spedine.server.domain.entity.ERole;

public record UserDashboardInfoDTO(
        String name,
        String email,
        ERole role
) {
}
