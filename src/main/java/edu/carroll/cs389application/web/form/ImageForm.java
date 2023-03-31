package edu.carroll.cs389application.web.form;

import edu.carroll.cs389application.service.ImageServiceImpl;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class ImageForm {
    private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    private MultipartFile imageFile;

    /**
     *
     * @param imageFile
     */
    public ImageForm(MultipartFile imageFile) {
        this.imageFile = imageFile;
        log.info("ImageForm created for image {}", imageFile.getOriginalFilename());
    }

    /**
     *
     * @return
     */
    public MultipartFile getImageFile() {
        return imageFile;
    }

    /**
     *
     * @param newImage
     */
    public void setImageFile(MultipartFile newImage) {
        this.imageFile = newImage;
    }
}