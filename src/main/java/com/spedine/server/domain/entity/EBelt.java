package com.spedine.server.domain.entity;

public enum EBelt {

    WHITE("Branca"),
    BLUE("Azul"),
    BLUE_GRADE("Azul-Grau"),
    YELLOW("Amarela"),
    YELLOW_GRADE("Amarela-Grau"),
    ORANGE("Laranja"),
    ORANGE_GRADE("Laranja-Grau"),
    GREEN("Verde"),
    GREEN_GRADE("Verde-Grau"),
    BROWN("Marrom"),
    BROWN_GRADE("Marrom-Grau"),
    BLACK("Preta"),
    BLACK_1_GRADE("Preta 1° Grau"),
    BLACK_2_GRADE("Preta 2° Grau"),
    BLACK_3_GRADE("Preta 3° Grau"),
    BLACK_4_GRADE("Preta 4° Grau"),
    BLACK_5_GRADE("Preta 5° Grau"),
    BLACK_6_GRADE("Preta 6° Grau"),
    BLACK_7_GRADE("Preta 7° Grau");

    private final String description;

    EBelt(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
