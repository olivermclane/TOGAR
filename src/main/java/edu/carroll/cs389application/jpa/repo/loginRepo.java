package edu.carroll.cs389application.jpa.repo;

import java.util.List;

import edu.carroll.cs389application.jpa.model.login;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface loginRepo extends JpaRepository<login, Integer> {
    List<login> findByUsernameIgnoreCase(String username);
}