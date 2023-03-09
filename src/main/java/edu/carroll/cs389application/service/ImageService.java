package edu.carroll.cs389application.service;

import edu.carroll.cs389application.jpa.model.Login;
import edu.carroll.cs389application.jpa.model.UserImage;
import edu.carroll.cs389application.web.form.ImageForm;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

public interface ImageService {

    /*
     *
     */
    void saveImage(ImageForm imageForm, Login user) throws IOException;

    void saveImageFile(UserImage userImage, MultipartFile file) throws IOException;

    List<Pair<InputStream, String>> pullImages(Login user) throws IOException;

    String validateFile (MultipartFile file);
}