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
 * Implementation of ImageService interface for saving to DB,uploading,validating and  user images.
 */
@Service
public class ImageServiceImpl implements ImageService {
    /**
     * This is a logging library that is used to keep track of logs for my application.
     */
    private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);
    /**
     * Repository interface for image entities. Provides additional query methods
     * to find images by user.
     */
    private final ImagesRepo imageRepo;

    /**
     * Constructor that initializes ImagesRepo instance.
     *
     * @param imageR ImagesRepo instance to be used to access imagedata database.
     */
    public ImageServiceImpl(ImagesRepo imageR) {
        this.imageRepo = imageR;
    }

    /**
     * Change this to boolean
     * This method will take an image and strip it of all the image metadata, and then save the data in our DB.
     * At the end of the method we have it call the saveImageFile command which will write the image to disk under the given location.
     *
     * @param imageForm Data from the Image Page including the image file.
     * @param user      User that the image will be saved and linked too.
     */
    @Override
    public void saveImage(ImageForm imageForm, Login user) {
        try {
            MultipartFile imageFile = imageForm.getImageFile();

            if (!validateFile(imageFile).equals(ErrorCode.VALID_FILE)) {
                log.error("User uploaded invalid file without using form validation on client-side -- UserID: {}", user.getId());
            } else {
                String uploadDirectory = "/Users/olivermclane/Desktop/TogarImageDirectory/";

                //Setting up the properties for a UserImage object
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
                saveImageFile(uImage, imageFile);
                imageRepo.save(uImage);
                log.info("Success: Image meta data created in user_image");
            }
        } catch (IOException e) {
            //Create out if statements for different types of IOExceptions and log them
            if (e instanceof FileAlreadyExistsException) {
                log.error("User Saved Image that already exist under that name...");
            }
            log.error("Problem saving file follow exception: {}", e.toString());
        }
    }

    /**
     * This method will write the multipart file to the disk under the DataBase file location
     * for the image. The "files" library takes care of saving the image to the disk using the InputStream.
     *
     * @param userImage Instance of the userImage to grab the location to save the file.
     * @param file      This is the file to be saved to disk.
     */
    private void saveImageFile(UserImage userImage, MultipartFile file) {
        try {
            //File Location
            String imageLocation = userImage.getImageLocation();

            //Converts the string to a Path
            Path root = Paths.get(imageLocation);

            //Create and saves the image to the path
            Files.createDirectories(root);
            root = root.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), root);
            log.info("Image has been saved to directory image");
        } catch (IOException e) {
            //Catch any errors and logs a IO happened
            log.error("User caused a IOException review logs: {}", e.toString());
        }

    }

    /**
     * This method will use the user login information to access the database and return the
     * images in a "Pair" data structure imported from the data.util.Pair. In the Pair we have the
     * input stream of the file resource and the string conversion of the file content-type.
     *
     * @param user Instance of the user currently logged into the app
     * @return Pair data structure on the left being the input stream of the file and on the right being the Content-type of the resource
     */
    @Override
    public List<Pair<InputStream, String>> pullImages(Login user) {
        //List of UserImages from the imageRepo for the selected user
        List<UserImage> images = loadUserImagesbyUserID(user);

        //Instance of return inputstream, content-type Pair.
        List<Pair<InputStream, String>> imageStreams = new ArrayList<>();

        try {
            //Logging userID and total images for user
            log.info("User ID: {}", user.getId());
            log.info("Images list size: {}", images.size());

            //Loop through all the UserImage instance in our image list
            for (UserImage image : images) {

                //Image location
                String imageLocation = image.getImageLocation() + image.getImageName();
                log.info("Image pulled: {} ", imageLocation);

                //Image extension
                String extension = image.getImageName().substring(image.getImageName().lastIndexOf(".") + 1);

                //If the image is a valid content-type (jpg,jpeg,png)
                if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("png")) {
                    //the inputstream of the file from filesystem grab
                    InputStream inputStream = new FileInputStream(imageLocation);

                    //content-type
                    String contentType = extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg") ? "image/jpeg" : "image/png";

                    //Append the input stream and content-type to our Pair
                    imageStreams.add(Pair.of(inputStream, contentType));
                }
            }
        } catch (IOException e) {
            //Catch errors of loading files
            log.error("User has caused a IOException review logs: {}", e.toString());
        }
        return imageStreams;
    }

    /**
     * This method will validate any type of multipart file and return a ErrorCode for either being
     * null, non-existent, too large, and improper content-type. Unless the file is valid in which case
     * the method will return a ErrorCode for a valid file.
     *
     * @param file Instance of file to validate against errors
     * @return This will the type of ErrorCode to throw or don't throw at the user.
     */
    @Override
    public ErrorCode validateFile(MultipartFile file) {
        //Check for null file
        if (file == null || file.getContentType() == null) {
            log.error("User Error: User upload a null file.");
            return ErrorCode.INVALID_FILE_ISNULL;
        }

        //Check for any file selected
        if (file.isEmpty()) {
            log.error("User Error: User uploaded a empty file.");
            return ErrorCode.INVALID_FILE_EMPTY;
        }

        //Check content-type
        if (!file.getContentType().startsWith("image/")) {
            log.error("User Error: Image isn't a valid image type ex.(image/). Image type was {}.", file.getContentType());
            return ErrorCode.INVALID_FILE_TYPE;
        }

        //Check file size
        if (file.getSize() > 10 * 1024 * 1024) {
            log.error("User Error: Image was too large to upload over 10MB.");
            return ErrorCode.INVALID_FILE_SIZE;
        }

        //Valid file
        return ErrorCode.VALID_FILE;
    }

    /**
     * Returns a list of UserImage objects for a given user.
     *
     * @param user User whose images should be retrieved
     * @return a List of UserImage objects associated with the given user
     */
    public List<UserImage> loadUserImagesbyUserID(Login user) {
        return imageRepo.findByUser(user);
    }

    /**
     * Deletes all userimage entities within the image database.
     *
     * @param user Login entity to delete by.
     */
    public void deleteUserImages(Login user) {
        imageRepo.deleteByUser(user);
    }

    /**
     * This enum represents the possible error codes that may occur during the validation of an image file.
     */
    public enum ErrorCode {
        /**
         * This is our ErrorCode for invalid file extension.
         */
        INVALID_FILE_TYPE("INVALID: Check file extension (.png or .jpg)"),

        /**
         * This is our ErrorCode if user empty file is uploaded.
         */
        INVALID_FILE_EMPTY("Please select a image file."),

        /**
         * This is our ErrorCode for a null file.
         */
        INVALID_FILE_ISNULL("INVALID: You have provided a broken file"),

        /**
         * This is our ErrorCode for file that exceeds our size limit.
         */
        INVALID_FILE_SIZE("INVALID: Image is too large."),

        /**
         * This is the ErrorCode for a valid.
         */
        VALID_FILE("validFile");

        /**
         * This is the string that holds the error of the ErrorCode.
         */
        private final String error;

        /**
         * Constructs an error code object with the given error message.
         *
         * @param error the error message for this error code
         */
        ErrorCode(String error) {
            this.error = error;
        }

        /**
         * Returns the error message for this error code.
         *
         * @return the error message for this error code
         */
        public String toString() {
            return error;
        }
    }
}