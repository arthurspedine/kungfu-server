package com.spedine.server.domain.entity;

import jakarta.persistence.*;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Belt belt = (Belt) o;
        return Objects.equals(id, belt.id) && name == belt.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public Long getId() {
        return id;
    }

    public EBelt getName() {
        return name;
    }

}
