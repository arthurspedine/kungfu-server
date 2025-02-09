package com.spedine.server.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StudentBelt that = (StudentBelt) o;
        return Objects.equals(student, that.student) && Objects.equals(belt, that.belt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, belt);
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

