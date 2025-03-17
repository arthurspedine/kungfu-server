package com.spedine.server.dto;

import java.util.UUID;

public record UserListingInfoDTO(
        UUID id,
        String name
) {
}
