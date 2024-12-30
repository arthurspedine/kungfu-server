package com.spedine.server.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "belts")
public class Belt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "name")
    private EBelt name;

    public Belt() {
    }

    public Belt(EBelt EBelt) {
        this.name = EBelt;
    }

    public Long getId() {
        return id;
    }

    public EBelt getName() {
        return name;
    }

}
