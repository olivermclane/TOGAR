package edu.carroll.cs389application.service;

import edu.carroll.cs389application.jpa.model.Login;
import edu.carroll.cs389application.jpa.model.UserImage;
import edu.carroll.cs389application.jpa.repo.ImagesRepo;
import edu.carroll.cs389application.web.form.ImageForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Service
public class ImageServiceImpl implements ImageService {
    private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);
    private final ImagesRepo imageRepo;

    /**
     *
     */
    public ImageServiceImpl(ImagesRepo imageR) {
        this.imageRepo = imageR;
    }

    /**
     *
     * @param imageForm
     * @param user
     * @throws IOException
     */
    @Override
    public void saveImage(ImageForm imageForm, Login user) throws IOException {
        MultipartFile imageFile = imageForm.getImageFile();
        String uploadDirectory = "./images/";

        //Seting up the properties for a UserImage object
        String imageName = imageFile.getOriginalFilename();
        String imageLocation = uploadDirectory + user.getId() + "/";
        String extension = StringUtils.getFilenameExtension(imageName);
        long imageSize = imageFile.getSize();

        //Image processing library
        BufferedImage bImage = ImageIO.read(imageFile.getInputStream());
        int imageHeight = bImage.getHeight();
        int imageWidth = bImage.getWidth();

        //creating userImage
        UserImage uImage = new UserImage(imageName, extension, imageLocation, imageSize, imageHeight, imageWidth, user);
        uImage.setImageFile(imageFile);

        //Saving UserImage object to DB and to the location on the local DB


        try {
            saveImageFile(uImage, imageFile);
            imageRepo.save(uImage);
            log.info("Success: Image meta data created in user_image");
        } catch (IOException e) {
            //Create out if statements for different types of IOExceptions and log them
            if (e instanceof FileAlreadyExistsException) {
                log.error("A file of that name already exists.");
            }
            log.error("Problem saving file follow exception");
        }
    }

    /**
     *
     * @param userImage
     * @param file
     * @throws IOException
     */
    @Override
    public void saveImageFile(UserImage userImage, MultipartFile file) throws IOException {
        String imageLocation = userImage.getImageLocation();
        Path root = Paths.get(imageLocation);
        Files.createDirectories(root);
        root = root.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), root);
        log.info("Image has been saved to directory image");

    }

    /**
     *
     * @param user
     * @return
     * @throws IOException
     */
    @Override
    public List<Pair<InputStream, String>> pullImages(Login user) throws IOException {
        List<UserImage> images = imageRepo.findByUser(user);
        log.info("User ID: {}", user.getId());
        log.info("Images list size: {}", images.size());
        List<Pair<InputStream, String>> imageStreams = new ArrayList<>();

        for (UserImage image : images) {
            String imageLocation = image.getImageLocation() + image.getImageName();
            log.info("Image pulled: {} ", imageLocation);
            String extension = image.getImageName().substring(image.getImageName().lastIndexOf(".") + 1);
            if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("png")) {
                InputStream inputStream = new FileInputStream(imageLocation);
                String contentType = extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg") ? "image/jpeg" : "image/png";
                imageStreams.add(Pair.of(inputStream, contentType));
            }
        }

        return imageStreams;
    }

    /**
     *
     * @param file
     * @return
     */
    @Override
    public ErrorCode validateFile(MultipartFile file) {
        if (file == null) {
            log.error("User Error:" + ErrorCode.INVALID_FILE_ISNULL.toString());
            return ErrorCode.INVALID_FILE_ISNULL;
        }
        if (file.isEmpty()) {
            log.error("User Error:" + ErrorCode.INVALID_FILE_EMPTY.toString());
            return ErrorCode.INVALID_FILE_EMPTY;
        }
        if (file.getSize() > 10 * 1024 * 1024) {
            log.error("User Error:" + ErrorCode.INVALID_FILE_SIZE.toString());
            return ErrorCode.INVALID_FILE_SIZE;
        }
        if (!file.getContentType().startsWith("image/")) {
            log.error("User Error:" + ErrorCode.INVALID_FILE_TYPE.toString());
            return ErrorCode.INVALID_FILE_TYPE;
        }
        return ErrorCode.VALID_FILE;
    }

    /**
     *
     * @param user
     * @return
     */
    public List<UserImage> loadUserImagesbyUserID(Login user) {
        return imageRepo.findByUser(user);
    }

    /**
     *
     */
    public enum ErrorCode {
        INVALID_FILE_TYPE("Invalid File Type: Validate image types are PNG and JPEG"),
        INVALID_FILE_EMPTY("Invalid File: Content of file is empty, or you didn't upload a file"),
        INVALID_FILE_ISNULL("Invalid File: File is null, make sure you upload a valid file"),
        INVALID_FILE_SIZE("Invalid File Size: Validate image size is less than 10MB"),
        VALID_FILE("validFile");


        private final String error;

        /**
         *
         * @param error
         */
        ErrorCode(String error) {
            this.error = error;
        }

        public String toString() {
            return error;
        }
    }
}