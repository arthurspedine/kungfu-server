package com.spedine.server.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, columnDefinition = "DEFAULT 1")
    private Boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ESex sex;

    @ManyToOne
    @JoinColumn(name = "training_center_id")
    private TrainingCenter trainingCenter;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @OrderBy("achievedDate DESC")
    private List<StudentBelt> belts = new ArrayList<>();

    public Student() {
    }

    public Belt getCurrentBelt() {
        return belts.stream()
                .max(Comparator.comparing(StudentBelt::getAchievedDate))
                .map(StudentBelt::getBelt)
                .orElse(null);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ESex getSex() {
        return sex;
    }

    public void setSex(ESex sex) {
        this.sex = sex;
    }

    public List<StudentBelt> getBelts() {
        return belts;
    }

    public TrainingCenter getTrainingCenter() {
        return trainingCenter;
    }

    public void setTrainingCenter(TrainingCenter trainingCenter) {
        this.trainingCenter = trainingCenter;
    }
}
