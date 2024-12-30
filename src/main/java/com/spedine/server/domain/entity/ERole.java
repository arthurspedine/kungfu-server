package com.spedine.server.domain.entity;

public enum ERole {

    TEACHER("Professor"),
    MASTER("Mestre");

    private final String description;

    ERole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
