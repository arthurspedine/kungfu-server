package com.spedine.server.domain.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

@Entity
@Table(name = "users")
@PrimaryKeyJoinColumn(name = "id")
public class User extends Student implements UserDetails {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private ERole role;

    @OneToOne(mappedBy = "teacher")
    private TrainingCenter trainingCenter;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public boolean isMaster() {
        return this.role == ERole.MASTER;
    }

    public boolean hasTeacherRole() {
        return role == ERole.TEACHER || role == ERole.MASTER;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        ERole[] roles = ERole.values();

        int currentIndex = this.getRole().ordinal();

        return Arrays.stream(roles)
                .filter(role -> role.ordinal() <= currentIndex)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(ERole role) {
        this.role = role;
    }

    public ERole getRole() {
        return role;
    }

    @Override
    public TrainingCenter getTrainingCenter() {
        return trainingCenter;
    }

    @Override
    public void setTrainingCenter(TrainingCenter trainingCenter) {
        this.trainingCenter = trainingCenter;
    }
}
