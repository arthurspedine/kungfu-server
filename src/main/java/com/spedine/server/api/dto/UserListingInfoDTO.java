package com.spedine.server.api.dto;

import java.util.UUID;

public record UserListingInfoDTO(
        UUID id,
        String name
) {
}
