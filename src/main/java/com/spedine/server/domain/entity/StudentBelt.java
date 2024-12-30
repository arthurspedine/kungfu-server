package com.spedine.server.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "student_belts")
public class StudentBelt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "belt_id", referencedColumnName = "id", nullable = false)
    private Belt belt;

    @Column(nullable = false)
    private LocalDate achievedDate;

    public StudentBelt() {
    }

    public StudentBelt(Student student, Belt belt, LocalDate achievedDate) {
        this.student = student;
        this.belt = belt;
        this.achievedDate = achievedDate;
    }

    public UUID getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Belt getBelt() {
        return belt;
    }

    public void setBelt(Belt belt) {
        this.belt = belt;
    }

    public LocalDate getAchievedDate() {
        return achievedDate;
    }

    public void setAchievedDate(LocalDate achievedDate) {
        this.achievedDate = achievedDate;
    }
}

