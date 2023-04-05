package edu.carroll.cs389application.web.form;

import edu.carroll.cs389application.service.ImageServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * The ImageForm class represents a form used for uploading images.
 * It contains a single MultipartFile field for the image file and a logger for logging events.
 */
public class ImageForm {

    /**
     * This is a basic logging platform that we use throughout the application for logging errors,info, and warnings.
     */
    private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    /**
     * This is the multipart file that is an image.
     */
    private MultipartFile imageFile;

    /**
     * Creates a new instance of the ImageForm class with a given image file.
     *
     * @param imageFile the image file to be assigned to the imageFile field
     */
    public ImageForm(MultipartFile imageFile) {
        this.imageFile = imageFile;
        log.info("ImageForm created for image {}", imageFile.getOriginalFilename());
    }

    /**
     * Returns the value of the imageFile field.
     *
     * @return the value of the imageFile field
     */
    public MultipartFile getImageFile() {
        return imageFile;
    }

    /**
     * Sets the value of the imageFile field to a new image file.
     *
     * @param newImage the new image file to be assigned to the imageFile field
     */
    public void setImageFile(MultipartFile newImage) {
        this.imageFile = newImage;
    }
}