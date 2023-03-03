package edu.carroll.cs389application.jpa.repo;

import edu.carroll.cs389application.jpa.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 *
 */
@Repository
public interface LoginRepo extends JpaRepository<Login, Integer> {
    List<Login> findByUsernameIgnoreCase(String username);
}