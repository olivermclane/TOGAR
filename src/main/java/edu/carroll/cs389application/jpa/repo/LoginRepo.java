package edu.carroll.cs389application.jpa.repo;

import java.util.List;

import edu.carroll.cs389application.jpa.model.Login;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 *
 */
public interface LoginRepo extends JpaRepository<Login, Integer> {
    List<Login> findByUsernameIgnoreCase(String username);
}