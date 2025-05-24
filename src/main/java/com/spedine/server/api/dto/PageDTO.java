package com.spedine.server.api.dto;

import java.util.List;

public record PageDTO<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
