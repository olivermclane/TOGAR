package edu.carroll.cs389application.jpa.repo;

import edu.carroll.cs389application.jpa.model.Login;
import edu.carroll.cs389application.jpa.model.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagesRepo extends JpaRepository<UserImage, Long> {
    List<UserImage> findByUser(Login user);
}