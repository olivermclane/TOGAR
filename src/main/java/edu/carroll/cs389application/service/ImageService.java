/**
 * Provides services for managing images uploaded by users, including saving, loading, and validating image files.
 */
package edu.carroll.cs389application.service;

import edu.carroll.cs389application.jpa.model.Login;
import edu.carroll.cs389application.jpa.model.UserImage;
import edu.carroll.cs389application.web.form.ImageForm;
import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;
import edu.carroll.cs389application.service.ImageServiceImpl.ErrorCode;

import java.io.InputStream;
import java.util.List;

/**
 * Defines methods for managing images uploaded by users.
 */
public interface ImageService {

    /**
     * Saves an image and its metadata for the given user.
     *
     * @param imageForm the form data for the image to be saved
     * @param user      the user who uploaded the image
     */
    void saveImage(ImageForm imageForm, Login user);

    /**
     * Retrieves the images uploaded by the given user.
     *
     * @param user the user whose images to retrieve
     * @return a list of pairs of input streams and file names for the user's images
     */
    List<Pair<InputStream, String>> pullImages(Login user);

    /**
     * Validates the given file to ensure that it is a valid image file.
     *
     * @param file the MultipartFile to validate
     * @return the validation error code
     */
    ErrorCode validateFile(MultipartFile file);

    /**
     * Retrieves the images uploaded by the given user.
     *
     * @param user the user whose images to retrieve
     * @return a list of UserImage objects representing the user's images
     */
    List<UserImage> loadUserImagesbyUserID(Login user);
}
