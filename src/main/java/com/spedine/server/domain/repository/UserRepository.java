package com.spedine.server.domain.repository;

import com.spedine.server.api.dto.UserListingInfoDTO;
import com.spedine.server.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    UserDetails findByEmail(String email);

    @Query("SELECT new com.spedine.server.api.dto.UserListingInfoDTO(u.id, s.name) " +
            "FROM User u " +
            "JOIN Student s " +
            "ON u.id = s.id " +
            "WHERE u.role != 'ADMIN'")
    List<UserListingInfoDTO> listAllUsers();
}
