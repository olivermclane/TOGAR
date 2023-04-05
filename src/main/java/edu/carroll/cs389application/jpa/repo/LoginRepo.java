/**
 * This package contains repository interfaces for interacting with the Login entity in the database.
 */
package edu.carroll.cs389application.jpa.repo;

import edu.carroll.cs389application.jpa.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 This interface extends the JpaRepository interface for the Login entity, providing CRUD functionality and
 additional methods for finding Login entities by username.
 */
@Repository
public interface LoginRepo extends JpaRepository<Login, Integer> {

    /**
     Finds a list of Login entities by their username, ignoring case.
     @param username the username to search for
     @return a list of Login entities with the given username, or an empty list if none are found
     */
    List<Login> findByUsernameIgnoreCase(String username);
}