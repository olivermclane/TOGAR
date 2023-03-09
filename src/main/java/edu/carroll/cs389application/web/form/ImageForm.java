package edu.carroll.cs389application.web.form;

import edu.carroll.cs389application.service.ImageServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

public class ImageForm {
    private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    @NotNull(message = "File image cannot be null")
    @Size(max = 10000000, message = "File size shouldn't exceed 2 MB")
    private MultipartFile imageFile;

    public ImageForm(MultipartFile imageFile) {
        if (!imageFile.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }
        this.imageFile = imageFile;
        log.info("ImageForm created for image {}", imageFile.getOriginalFilename());
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }


    public void setImageFile(MultipartFile newImage) {
        if (!imageFile.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }
        this.imageFile = newImage;
    }
}