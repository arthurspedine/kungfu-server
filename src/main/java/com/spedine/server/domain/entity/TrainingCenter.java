package com.spedine.server.domain.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "training_center")
public class TrainingCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @OneToMany(mappedBy = "trainingCenter")
    private Set<Student> students = new HashSet<>();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private Integer number;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false, length = 8)
    private String zipCode;

    public TrainingCenter() {
    }

    public void addStudent(Student student) {
        students.add(student);
        student.setTrainingCenter(this);
    }

    public void removeStudent(Student student) {
        students.remove(student);
        student.setTrainingCenter(null);
    }

    public boolean canBeModifiedBy(User user) {
        return user.isMaster() || user.getId() == this.teacher.getId();
    }

    public UUID getId() {
        return id;
    }

    public Student getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return this.zipCode.substring(0, 4) + this.zipCode.substring(5);
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
