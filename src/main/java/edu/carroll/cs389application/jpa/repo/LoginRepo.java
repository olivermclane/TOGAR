package edu.carroll.cs389application.jpa.repo;

import edu.carroll.cs389application.jpa.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 *
 */
public interface LoginRepo extends JpaRepository<Login, Integer> {
    List<Login> findByUsernameIgnoreCase(String username);
}