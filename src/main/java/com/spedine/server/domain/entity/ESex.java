package com.spedine.server.domain.entity;

public enum ESex {

    M("Masculino"), F("Feminino");

    private final String description;

    ESex(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
