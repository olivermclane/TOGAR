/**
 * This package contains repository interfaces for JPA entities related to image data.
 */
package edu.carroll.cs389application.jpa.repo;

import edu.carroll.cs389application.jpa.model.Login;
import edu.carroll.cs389application.jpa.model.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This interface extends JpaRepository for UserImage entity and provides additional query methods
 * to find images by user.
 */
@Repository
public interface ImagesRepo extends JpaRepository<UserImage, Long> {

    /**
     * Finds a list of UserImage entities associated with the specified user.
     *
     * @param user the Login entity to search for
     * @return a list of UserImage entities associated with the specified user
     */
    List<UserImage> findByUser(Login user);
}


