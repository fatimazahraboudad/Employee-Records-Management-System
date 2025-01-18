package com.example.EmployeeRecordsManagementSystem.repositories;

import com.example.EmployeeRecordsManagementSystem.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmailIgnoreCase(String email);
    User findByEmail(String email);

}
