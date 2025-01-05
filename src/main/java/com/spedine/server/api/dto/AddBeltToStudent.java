package com.spedine.server.api.dto;

import java.util.UUID;

public record AddBeltToStudent(
        UUID studentId,
        BeltDataDTO belt
) {
}
